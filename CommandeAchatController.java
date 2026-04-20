package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.CommandeAchat;
import com.backoffice.atelier.entities.LigneCommandeAchat;
import com.backoffice.atelier.repositories.CommandeAchatRepository;
import com.backoffice.atelier.services.DocumentAchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes-achat")
@RequiredArgsConstructor
public class CommandeAchatController {

    private final CommandeAchatRepository commandeAchatRepository;
    private final DocumentAchatService documentAchatService;

    @GetMapping
    public List<CommandeAchat> getAllCommandes() {
        return commandeAchatRepository.findAll();
    }

    @GetMapping("/{id}")
    public CommandeAchat getCommandeById(@PathVariable Long id) {
        return commandeAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    public List<CommandeAchat> getCommandesByFournisseur(@PathVariable Long fournisseurId) {
        return commandeAchatRepository.findByFournisseur_Id(fournisseurId);
    }

    @GetMapping("/statut/{statut}")
    public List<CommandeAchat> getCommandesByStatut(
            @PathVariable CommandeAchat.StatutCommande statut) {
        return commandeAchatRepository.findByStatut(statut);
    }

    @PostMapping
    public ResponseEntity<CommandeAchat> createCommande(@RequestBody CommandeAchat commande) {
        if (commande.getNumero() == null || commande.getNumero().isBlank()) {
            commande.setNumero("CA-" + System.currentTimeMillis());
        }
        if (commande.getStatut() == null) {
            commande.setStatut(CommandeAchat.StatutCommande.BROUILLON);
        }

        if (commande.getLignes() != null && !commande.getLignes().isEmpty()) {
            for (LigneCommandeAchat ligne : commande.getLignes()) {
                ligne.setCommandeAchat(commande);

                // Récupérer la quantité depuis l'un ou l'autre champ
                Integer qte = ligne.getQuantiteDemandee() != null
                        ? ligne.getQuantiteDemandee()
                        : ligne.getQuantiteCommandee();
                if (qte == null) qte = 1;

                // Remplir les deux colonnes NOT NULL
                ligne.setQuantiteDemandee(qte);
                ligne.setQuantiteCommandee(qte);

                if (ligne.getMontantLigne() == null && ligne.getPrixUnitaire() != null) {
                    ligne.setMontantLigne(qte * ligne.getPrixUnitaire());
                }
            }
            double ht = commande.getLignes().stream()
                    .mapToDouble(l -> l.getMontantLigne() != null ? l.getMontantLigne() : 0.0)
                    .sum();
            commande.setMontantHT(ht);
            commande.setMontantTVA(ht * 0.19);
            commande.setMontantTTC(ht * 1.19);
        }

        return ResponseEntity.ok(commandeAchatRepository.save(commande));
    }

    @PutMapping("/{id}")
    public CommandeAchat updateCommande(
            @PathVariable Long id,
            @RequestBody CommandeAchat commande) {
        CommandeAchat existing = commandeAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        existing.setNumero(commande.getNumero());
        existing.setFournisseur(commande.getFournisseur());
        existing.setDateCommande(commande.getDateCommande());
        existing.setDateArriveePrevue(commande.getDateArriveePrevue());
        existing.setStatut(commande.getStatut());
        existing.setMontantHT(commande.getMontantHT());
        existing.setMontantTVA(commande.getMontantTVA());
        existing.setMontantTTC(commande.getMontantTTC());
        existing.setObservation(commande.getObservation());

        return commandeAchatRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteCommande(@PathVariable Long id) {
        commandeAchatRepository.deleteById(id);
    }

    @PostMapping("/depuis-demande/{demandePrixId}")
    public CommandeAchat createCommandeFromDemande(@PathVariable Long demandePrixId) {
        return documentAchatService.creerCommandeDepuisDemande(demandePrixId);
    }
}