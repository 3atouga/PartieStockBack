package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.BonLivraison;  // ✅ Changé
import com.backoffice.atelier.entities.FactureVente;
import com.backoffice.atelier.repositories.BonLivraisonRepository;
import com.backoffice.atelier.repositories.FactureVenteRepository;
import com.backoffice.atelier.services.DocumentVenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentVenteController {

    private final DocumentVenteService documentVenteService;
    private final BonLivraisonRepository bonLivraisonRepository;
    private final FactureVenteRepository factureVenteRepository;

    // ========== BONS DE LIVRAISON ==========

    @GetMapping("/bons-livraison")
    public List<BonLivraison> getAllBonsLivraison() {  // ✅ Changé
        return bonLivraisonRepository.findAll();
    }

    @GetMapping("/bons-livraison/{id}")
    public BonLivraison getBLById(@PathVariable Long id) {  // ✅ Changé
        return bonLivraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BL non trouvé"));
    }

    @PostMapping("/bons-livraison")
    public BonLivraison creerBL(@RequestBody BonLivraison bonLivraison) {  // ✅ Changé
        return documentVenteService.creerBonLivraison(bonLivraison);
    }

    @PutMapping("/bons-livraison/{id}")
    public BonLivraison updateBL(@PathVariable Long id, @RequestBody BonLivraison bonLivraison) {  // ✅ Changé
        bonLivraison.setId(id);
        return documentVenteService.creerBonLivraison(bonLivraison);
    }

    @PostMapping("/bons-livraison/{id}/expedier")
    public BonLivraison expedierBL(@PathVariable Long id) {  // ✅ Changé
        return documentVenteService.expedierBL(id);
    }

    // ========== FACTURES VENTE ==========

    @GetMapping("/factures-vente")
    public List<FactureVente> getAllFacturesVente() {
        return factureVenteRepository.findAll();
    }

    @GetMapping("/factures-vente/{id}")
    public FactureVente getFactureVenteById(@PathVariable Long id) {
        return factureVenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture non trouvée"));
    }

    @PostMapping("/factures-vente/depuis-bl/{blId}")
    public FactureVente creerFactureDepuisBL(@PathVariable Long blId) {
        return documentVenteService.creerFactureDepuisBL(blId);
    }

    @PostMapping("/factures-vente/{id}/valider")
    public FactureVente validerFacture(@PathVariable Long id) {
        return documentVenteService.validerFactureVente(id);
    }
}