package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.FactureAchat;
import com.backoffice.atelier.entities.LigneFactureAchat;
import com.backoffice.atelier.repositories.FactureAchatRepository;
import com.backoffice.atelier.services.DocumentAchatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factures-achat")
@RequiredArgsConstructor
public class FactureAchatController {

    private final FactureAchatRepository factureAchatRepository;
    private final DocumentAchatService documentAchatService;

    @GetMapping
    public List<FactureAchat> getAllFacturesAchat() {
        return factureAchatRepository.findAll();
    }

    @GetMapping("/{id}")
    public FactureAchat getFactureAchatById(@PathVariable Long id) {
        return factureAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture d'achat non trouvée"));
    }

    @PostMapping("/depuis-commande/{commandeAchatId}")
    public FactureAchat creerFactureDepuisCommande(@PathVariable Long commandeAchatId) {
        return documentAchatService.creerFactureDepuisCommande(commandeAchatId);
    }

    @PostMapping("/{id}/valider")
    public FactureAchat validerFactureEtMAJStock(@PathVariable Long id) {
        return documentAchatService.validerFactureEtMAJStock(id);
    }

    @PostMapping
    public ResponseEntity<FactureAchat> createFactureAchat(@RequestBody FactureAchat factureAchat) {
        if (factureAchat.getNumero() == null || factureAchat.getNumero().isBlank()) {
            factureAchat.setNumero("FA-" + System.currentTimeMillis());
        }
        if (factureAchat.getStatut() == null) {
            factureAchat.setStatut(FactureAchat.StatutFacture.EN_ATTENTE);
        }
        if (factureAchat.getStockMisAJour() == null) {
            factureAchat.setStockMisAJour(false);
        }

        if (factureAchat.getLignes() != null && !factureAchat.getLignes().isEmpty()) {
            for (LigneFactureAchat ligne : factureAchat.getLignes()) {
                ligne.setFactureAchat(factureAchat);
                if (ligne.getMontantLigne() == null
                        && ligne.getQuantite() != null
                        && ligne.getPrixUnitaire() != null) {
                    ligne.setMontantLigne(ligne.getQuantite() * ligne.getPrixUnitaire());
                }
            }
            double ht = factureAchat.getLignes().stream()
                    .mapToDouble(l -> l.getMontantLigne() != null ? l.getMontantLigne() : 0.0)
                    .sum();
            factureAchat.setMontantHT(ht);
            factureAchat.setMontantTVA(ht * 0.19);
            factureAchat.setMontantTTC(ht * 1.19);
        }

        FactureAchat saved = factureAchatRepository.save(factureAchat);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public FactureAchat updateFactureAchat(@PathVariable Long id, @RequestBody FactureAchat factureAchat) {
        FactureAchat existing = factureAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture d'achat non trouvée"));

        existing.setFournisseur(factureAchat.getFournisseur());
        existing.setDateFacture(factureAchat.getDateFacture());
        existing.setDateEcheance(factureAchat.getDateEcheance());
        existing.setStatut(factureAchat.getStatut());
        existing.setObservation(factureAchat.getObservation());

        return factureAchatRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteFactureAchat(@PathVariable Long id) {
        factureAchatRepository.deleteById(id);
    }
}
