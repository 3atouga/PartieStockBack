package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.Categorie;
import com.backoffice.atelier.entities.Fournisseur;
import com.backoffice.atelier.repositories.CategorieRepository;
import com.backoffice.atelier.repositories.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
public class FournisseurController {

    private final FournisseurRepository fournisseurRepository;
    private final CategorieRepository categorieRepository;

    @GetMapping
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }

    @GetMapping("/{id}")
    public Fournisseur getFournisseurById(@PathVariable Long id) {
        return fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));
    }

    @GetMapping("/actifs")
    public List<Fournisseur> getFournisseursActifs() {
        return fournisseurRepository.findByActifTrue();
    }

    @PostMapping
    public Fournisseur createFournisseur(@RequestBody Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @PutMapping("/{id}")
    public Fournisseur updateFournisseur(@PathVariable Long id, @RequestBody Fournisseur fournisseur) {
        Fournisseur existing = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        existing.setCode(fournisseur.getCode());
        existing.setNom(fournisseur.getNom());
        existing.setMatriculeFiscale(fournisseur.getMatriculeFiscale());
        existing.setType(fournisseur.getType());
        existing.setEmail(fournisseur.getEmail());
        existing.setTelephone(fournisseur.getTelephone());
        existing.setAdresse(fournisseur.getAdresse());
        existing.setDelaiLivraisonJours(fournisseur.getDelaiLivraisonJours());
        existing.setJoursEcheance(fournisseur.getJoursEcheance());
        existing.setMethodeReglement(fournisseur.getMethodeReglement());
        existing.setNoteFournisseur(fournisseur.getNoteFournisseur());
        existing.setActif(fournisseur.isActif());

        return fournisseurRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteFournisseur(@PathVariable Long id) {
        fournisseurRepository.deleteById(id);
    }

    // ✅ VOIRFOURCATEG — Ajouter une catégorie à un fournisseur
    @PostMapping("/{id}/categories/{categorieId}")
    public ResponseEntity<Fournisseur> ajouterCategorie(
            @PathVariable Long id,
            @PathVariable Long categorieId) {

        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé: " + id));

        Categorie categorie = categorieRepository.findById(categorieId)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + categorieId));

        fournisseur.getCategories().add(categorie);
        return ResponseEntity.ok(fournisseurRepository.save(fournisseur));
    }

    // ✅ VOIRFOURCATEG — Retirer une catégorie d'un fournisseur
    @DeleteMapping("/{id}/categories/{categorieId}")
    public ResponseEntity<Fournisseur> retirerCategorie(
            @PathVariable Long id,
            @PathVariable Long categorieId) {

        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé: " + id));

        fournisseur.getCategories().removeIf(c -> c.getId().equals(categorieId));
        return ResponseEntity.ok(fournisseurRepository.save(fournisseur));
    }

    // ✅ VOIRFOURCATEG — Lister les catégories d'un fournisseur
    @GetMapping("/{id}/categories")
    public ResponseEntity<List<Categorie>> getCategories(@PathVariable Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé: " + id));
        return ResponseEntity.ok(List.copyOf(fournisseur.getCategories()));
    }
}
