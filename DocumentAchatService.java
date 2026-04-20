package com.backoffice.atelier.services;

import com.backoffice.atelier.entities.*;
import com.backoffice.atelier.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentAchatService {

    private final DemandePrixRepository demandePrixRepository;
    private final CommandeAchatRepository commandeAchatRepository;
    private final FactureAchatRepository factureAchatRepository;
    private final BonReceptionRepository bonReceptionRepository;
    private final ApparaitDansRepository apparaitDansRepository;
    private final StockService stockService;

    @Transactional
    public CommandeAchat creerCommandeDepuisDemande(Long demandePrixId) {

        DemandePrix demande = demandePrixRepository.findById(demandePrixId)
                .orElseThrow(() -> new RuntimeException("Demande de prix non trouvée"));

        if (demande.getStatut() != DemandePrix.StatutDocument.VALIDEE) {
            throw new RuntimeException("La demande de prix doit être VALIDÉE avant de créer une commande");
        }

        CommandeAchat commande = CommandeAchat.builder()
                .numero("CA-" + System.currentTimeMillis())
                .demandePrix(demande)
                .fournisseur(demande.getFournisseur())
                .dateCommande(LocalDate.now())
                .statut(CommandeAchat.StatutCommande.BROUILLON)
                .build();

        CommandeAchat savedCommande = commandeAchatRepository.save(commande);

        demande.getLignes().forEach(ligneDemande -> {
            Integer qte = ligneDemande.getQuantiteDemandee();
            Double prix = ligneDemande.getPrixUnitaire() != null ? ligneDemande.getPrixUnitaire() : 0.0;
            LigneCommandeAchat ligneCommande = LigneCommandeAchat.builder()
                    .commandeAchat(savedCommande)
                    .piece(ligneDemande.getPiece())
                    .quantiteDemandee(qte)
                    .quantiteCommandee(qte)
                    .prixUnitaire(prix)
                    .montantLigne(qte != null ? qte * prix : 0.0)
                    .build();
            savedCommande.getLignes().add(ligneCommande);
        });

        calculerTotauxCommande(savedCommande);
        CommandeAchat result = commandeAchatRepository.save(savedCommande);

        demande.getLignes().forEach(ligneDemande -> {
            boolean existe = apparaitDansRepository
                    .existsByDemandePrix_IdAndPiece_Id(demande.getId(), ligneDemande.getPiece().getId());
            if (!existe) {
                ApparaitDans lien = ApparaitDans.builder()
                        .demandePrix(demande)
                        .piece(ligneDemande.getPiece())
                        .commandeAchat(result)
                        .build();
                apparaitDansRepository.save(lien);
            }
        });

        log.info("Commande {} créée depuis DP {} avec {} lignes",
                result.getNumero(), demande.getNumero(), result.getLignes().size());
        return result;
    }

    @Transactional
    public FactureAchat creerFactureDepuisCommande(Long commandeAchatId) {

        CommandeAchat commande = commandeAchatRepository.findById(commandeAchatId)
                .orElseThrow(() -> new RuntimeException("Commande d'achat non trouvée"));

        FactureAchat facture = FactureAchat.builder()
                .numero("FA-" + System.currentTimeMillis())
                .commandeAchat(commande)
                .fournisseur(commande.getFournisseur())
                .dateFacture(LocalDate.now())
                .statut(FactureAchat.StatutFacture.EN_ATTENTE)
                .stockMisAJour(false)
                .build();

        FactureAchat savedFacture = factureAchatRepository.save(facture);

        commande.getLignes().forEach(ligneCommande -> {
            Integer qte = ligneCommande.getQuantiteCommandee() != null
                    ? ligneCommande.getQuantiteCommandee()
                    : ligneCommande.getQuantiteDemandee();
            Double prix = ligneCommande.getPrixUnitaire();

            LigneFactureAchat ligneFacture = LigneFactureAchat.builder()
                    .factureAchat(savedFacture)
                    .piece(ligneCommande.getPiece())
                    .quantite(qte)
                    .prixUnitaire(prix)
                    .montantLigne(qte != null && prix != null ? qte * prix : 0.0)
                    .build();
            savedFacture.getLignes().add(ligneFacture);
        });

        calculerTotauxFacture(savedFacture);
        FactureAchat result = factureAchatRepository.save(savedFacture);

        log.info("Facture {} créée depuis CA {} avec {} lignes",
                result.getNumero(), commande.getNumero(), result.getLignes().size());
        return result;
    }

    @Transactional
    public FactureAchat validerFactureEtMAJStock(Long factureId) {

        FactureAchat facture = factureAchatRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture d'achat non trouvée"));

        if (facture.isStockMisAJour()) {
            throw new RuntimeException("Le stock a déjà été mis à jour pour cette facture");
        }

        BonReception bonReception = null;
        if (facture.getCommandeAchat() != null) {
            Optional<BonReception> brOpt = bonReceptionRepository
                    .findByCommandeAchat_Id(facture.getCommandeAchat().getId())
                    .stream()
                    .filter(BonReception::isConforme)
                    .findFirst();
            bonReception = brOpt.orElse(null);
        }

        final BonReception finalBR = bonReception;

        facture.getLignes().forEach(ligne -> {
            stockService.incrementerStock(
                    ligne.getPiece(),
                    Emplacement.MAGASIN,
                    ligne.getQuantite(),
                    "FACTURE_ACHAT",
                    facture.getNumero()
            );
            if (finalBR != null) {
                stockService.creerMouvementEntree(
                        ligne.getPiece(),
                        ligne.getQuantite(),
                        finalBR
                );
            }
        });

        facture.setStatut(FactureAchat.StatutFacture.VALIDEE);
        facture.setStockMisAJour(true);

        FactureAchat saved = factureAchatRepository.save(facture);
        log.info("Facture {} validée et stock mis à jour", saved.getNumero());
        return saved;
    }

    private void calculerTotauxCommande(CommandeAchat commande) {
        double montantHT = commande.getLignes().stream()
                .mapToDouble(l -> l.getMontantLigne() != null ? l.getMontantLigne() : 0.0)
                .sum();
        commande.setMontantHT(montantHT);
        commande.setMontantTVA(montantHT * 0.19);
        commande.setMontantTTC(montantHT * 1.19);
    }

    private void calculerTotauxFacture(FactureAchat facture) {
        double montantHT = facture.getLignes().stream()
                .mapToDouble(l -> l.getMontantLigne() != null ? l.getMontantLigne() : 0.0)
                .sum();
        facture.setMontantHT(montantHT);
        facture.setMontantTVA(montantHT * 0.19);
        facture.setMontantTTC(montantHT * 1.19);
    }

    public List<FactureAchat> getFacturesOrderByDate() {
        return factureAchatRepository.findAllOrderByDateDesc();
    }
}