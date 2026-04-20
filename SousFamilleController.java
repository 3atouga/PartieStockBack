package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.SousFamille;
import com.backoffice.atelier.repositories.SousFamilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-familles")
@RequiredArgsConstructor
public class SousFamilleController {

    private final SousFamilleRepository sousFamilleRepository;

    @GetMapping
    public List<SousFamille> getAllSousFamilles() {
        return sousFamilleRepository.findAll();
    }

    @GetMapping("/{id}")
    public SousFamille getSousFamilleById(@PathVariable Long id) {
        return sousFamilleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sous-famille non trouvée"));
    }

    @GetMapping("/famille/{familleId}")
    public List<SousFamille> getSousFamillesByFamille(@PathVariable Long familleId) {
        return sousFamilleRepository.findByFamille_Id(familleId);
    }

    @PostMapping
    public SousFamille createSousFamille(@RequestBody SousFamille sousFamille) {
        return sousFamilleRepository.save(sousFamille);
    }

    @PutMapping("/{id}")
    public SousFamille updateSousFamille(@PathVariable Long id, @RequestBody SousFamille sousFamille) {
        SousFamille existing = sousFamilleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sous-famille non trouvée"));
        
        existing.setCode(sousFamille.getCode());
        existing.setLibelle(sousFamille.getLibelle());
        existing.setDescription(sousFamille.getDescription());
        existing.setFamille(sousFamille.getFamille());
        existing.setActif(sousFamille.isActif());
        
        return sousFamilleRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteSousFamille(@PathVariable Long id) {
        sousFamilleRepository.deleteById(id);
    }
}