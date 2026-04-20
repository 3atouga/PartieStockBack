package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.AvoirAchat;
import com.backoffice.atelier.entities.LigneAvoirAchat;
import com.backoffice.atelier.repositories.AvoirAchatRepository;
import com.backoffice.atelier.repositories.LigneAvoirAchatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avoirs-achat")
@RequiredArgsConstructor
public class AvoirAchatController {

    private final AvoirAchatRepository avoirAchatRepository;
    private final LigneAvoirAchatRepository ligneAvoirAchatRepository;

    @GetMapping
    public List<AvoirAchat> getAll() {
        return avoirAchatRepository.findAll();
    }

    @GetMapping("/{id}")
    public AvoirAchat getById(@PathVariable Long id) {
        return avoirAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avoir d'achat non trouvé"));
    }

    @GetMapping("/{id}/lignes")
    public List<LigneAvoirAchat> getLignes(@PathVariable Long id) {
        return ligneAvoirAchatRepository.findByAvoirAchat_Id(id);
    }

    @PostMapping
    public AvoirAchat create(@RequestBody AvoirAchat avoirAchat) {
        return avoirAchatRepository.save(avoirAchat);
    }

    @PutMapping("/{id}")
    public AvoirAchat update(@PathVariable Long id, @RequestBody AvoirAchat avoirAchat) {
        AvoirAchat existing = avoirAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avoir d'achat non trouvé"));

        existing.setNumero(avoirAchat.getNumero());
        existing.setFournisseur(avoirAchat.getFournisseur());
        existing.setFactureAchat(avoirAchat.getFactureAchat());
        existing.setDateAvoir(avoirAchat.getDateAvoir());
        existing.setTypeAvoir(avoirAchat.getTypeAvoir());
        existing.setMotif(avoirAchat.getMotif());
        existing.setStatut(avoirAchat.getStatut());

        return avoirAchatRepository.save(existing);
    }

    @PostMapping("/{id}/valider")
    public AvoirAchat valider(@PathVariable Long id) {
        AvoirAchat avoir = avoirAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avoir d'achat non trouvé"));

        if (avoir.getStatut() == AvoirAchat.StatutAvoir.VALIDE) {
            throw new RuntimeException("Cet avoir est déjà validé");
        }

        avoir.setStatut(AvoirAchat.StatutAvoir.VALIDE);
        return avoirAchatRepository.save(avoir);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        avoirAchatRepository.deleteById(id);
    }
}