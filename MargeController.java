package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.Marge;
import com.backoffice.atelier.repositories.MargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marges")
@RequiredArgsConstructor
public class MargeController {

    private final MargeRepository margeRepository;

    @GetMapping
    public List<Marge> getAllMarges() {
        return margeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Marge getMargeById(@PathVariable Long id) {
        return margeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marge non trouvée"));
    }

    @GetMapping("/actives")
    public List<Marge> getMargesActives() {
        return margeRepository.findByActifTrue();
    }

    @PostMapping	
    public Marge createMarge(@RequestBody Marge marge) {
        return margeRepository.save(marge);
    }

    @PutMapping("/{id}")
    public Marge updateMarge(@PathVariable Long id, @RequestBody Marge marge) {
        Marge existing = margeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Marge non trouvée"));
        
        existing.setNiveau(marge.getNiveau());
        existing.setTauxPourcentage(marge.getTauxPourcentage());
        existing.setActif(marge.isActif());
        
        return margeRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deleteMarge(@PathVariable Long id) {
        margeRepository.deleteById(id);
    }
}