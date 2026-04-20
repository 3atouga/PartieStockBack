package com.backoffice.atelier.services;

import com.backoffice.atelier.entities.*;
import com.backoffice.atelier.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentVenteService {

    private final BonLivraisonRepository bonLivraisonRepository;
    private final FactureVenteRepository factureVenteRepository;
    private final StockService stockService;

    // ✅ CRÉER BON DE LIVRAISON
    @Transactional
    public BonLivraison creerBonLivraison(BonLivraison bonLivraison) {
        if (bonLivraison.getNumero() == null || bonLivraison.getNumero().isEmpty()) {
            bonLivraison.setNumero("BL-" + System.currentTimeMillis());
        }
        calculerTotauxBL(bonLivraison);
        BonLivraison saved = bonLivraisonRepository.save(bonLivraison);
        log.info("BL créé: {}", saved.getNumero());
        return saved;
    }

    // ✅ EXPÉDIER BL
    @Transactional
    public BonLivraison expedierBL(Long blId) {
        BonLivraison bl = bonLivraisonRepository.findById(blId)
                .orElseThrow(() -> new RuntimeException("Bon de livraison non trouvé"));

        if (bl.isStockMisAJour()) {
            throw new RuntimeException("Le stock a déjà été mis à jour pour ce BL");
        }

        bl.getLignes().forEach(ligne ->
            stockService.decrementerStock(
                ligne.getPiece(),
                Emplacement.MAGASIN,
                ligne.getQuantite(),
                "BL_SORTANT",
                bl.getNumero()
            )
        );

        bl.setStatut(BonLivraison.StatutBL.EXPEDIE);
        bl.setStockMisAJour(true);

        BonLivraison saved = bonLivraisonRepository.save(bl);
        log.info("BL expédié: {}", saved.getNumero());
        return saved;
    }

    // ✅ CRÉER FACTURE VENTE DEPUIS BL
    @Transactional
    public FactureVente creerFactureDepuisBL(Long blId) {
        BonLivraison bl = bonLivraisonRepository.findById(blId)
                .orElseThrow(() -> new RuntimeException("Bon de livraison non trouvé"));

        FactureVente facture = FactureVente.builder()
                .numero("FV-" + System.currentTimeMillis())
                .bonLivraison(bl)
                .client(bl.getClient())
                .dateFacture(LocalDate.now())
                .statut(FactureVente.StatutFacture.BROUILLON)
                .build();

        // ✅ LigneFactureVente
        bl.getLignes().forEach(ligneBL -> {
            LigneFactureVente ligneFacture = LigneFactureVente.builder()
                    .factureVente(facture)
                    .piece(ligneBL.getPiece())
                    .quantite(ligneBL.getQuantite())
                    .prixUnitaire(ligneBL.getPrixUnitaire())
                    .build();
            facture.getLignes().add(ligneFacture);
        });

        calculerTotauxFacture(facture);

        FactureVente saved = factureVenteRepository.save(facture);
        log.info("Facture vente {} créée depuis BL {}", saved.getNumero(), bl.getNumero());
        return saved;
    }

    // ✅ VALIDER FACTURE VENTE
    @Transactional
    public FactureVente validerFactureVente(Long factureId) {
        FactureVente facture = factureVenteRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture de vente non trouvée"));

        if (facture.getStatut() == FactureVente.StatutFacture.VALIDEE) {
            throw new RuntimeException("Cette facture est déjà validée");
        }

        // ✅ LigneFactureVente
        facture.getLignes().forEach(ligne ->
            stockService.creerMouvementSortie(
                ligne.getPiece(),
                ligne.getQuantite(),
                facture
            )
        );

        facture.setStatut(FactureVente.StatutFacture.VALIDEE);
        FactureVente saved = factureVenteRepository.save(facture);
        log.info("Facture vente validée: {}", saved.getNumero());
        return saved;
    }

    // ✅ UTILITAIRES
    private void calculerTotauxBL(BonLivraison bl) {
        double montantHT = bl.getLignes().stream()
                .mapToDouble(l -> l.getMontantLigne() != null ? l.getMontantLigne() : 0.0)
                .sum();
        bl.setMontantHT(montantHT);
        bl.setMontantTVA(montantHT * 0.19);
        bl.setMontantTTC(montantHT * 1.19);
    }

    private void calculerTotauxFacture(FactureVente facture) {
        double montantHT = facture.getLignes().stream()
                .mapToDouble(l -> l.getMontantLigne() != null ? l.getMontantLigne() : 0.0)
                .sum();
        facture.setMontantHT(montantHT);
        facture.setMontantTVA(montantHT * 0.19);
        facture.setMontantTTC(montantHT * 1.19);
    }
}