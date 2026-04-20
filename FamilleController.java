package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.Famille;
import com.backoffice.atelier.repositories.FamilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/familles")
@RequiredArgsConstructor
public class FamilleController {

    private final FamilleRepository familleRepository;

    @GetMapping
    public List<Famille> getAllFamilles() {
        return familleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Famille getFamilleById(@PathVariable Long id) {
        return familleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Famille non trouvée"));
    }

    @GetMapping("/actives")
    public List<Famille> getFamillesActives() {
        return familleRepository.findByActifTrue();
    }

    @PostMapping
    public Famille createFamille(@RequestBody Famille famille) {
        return familleRepository.save(famille);
    }

    @PutMapping("/{id}")
    public Famille updateFamille(@PathVariable Long id, @RequestBody Famille famille) {
        Famille existing = familleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Famille non trouvée"));
        
        existing.setCode(famille.getCode());
        existing.setLibelle(famille.getLibelle());
        existing.setDescription(famille.getDescription());
        existing.setActif(famille.isActif());
        
        return familleRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteFamille(@PathVariable Long id) {
        familleRepository.deleteById(id);
    }
}