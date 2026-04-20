package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.BonReception;
import com.backoffice.atelier.repositories.BonReceptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bons-reception")
@RequiredArgsConstructor
public class BonReceptionController {

    private final BonReceptionRepository bonReceptionRepository;

    @GetMapping
    public List<BonReception> getAll() {
        return bonReceptionRepository.findAll();
    }

    @GetMapping("/{id}")
    public BonReception getById(@PathVariable Long id) {
        return bonReceptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bon de réception non trouvé"));
    }

    @PostMapping
    public BonReception create(@RequestBody BonReception bonReception) {
        return bonReceptionRepository.save(bonReception);
    }

    @PutMapping("/{id}")
    public BonReception update(@PathVariable Long id, @RequestBody BonReception bonReception) {
        BonReception existing = bonReceptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bon de réception non trouvé"));

        existing.setNumero(bonReception.getNumero());
        existing.setDateReception(bonReception.getDateReception());
        existing.setConforme(bonReception.isConforme());
        existing.setCommandeAchat(bonReception.getCommandeAchat());

        return bonReceptionRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bonReceptionRepository.deleteById(id);
    }
}