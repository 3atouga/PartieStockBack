package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.DemandePrix;
import com.backoffice.atelier.repositories.DemandePrixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandes-prix")
@RequiredArgsConstructor
public class DemandePrixController {

    private final DemandePrixRepository demandePrixRepository;

    @GetMapping
    public List<DemandePrix> getAllDemandesPrix() {
        return demandePrixRepository.findAll();
    }

    @GetMapping("/{id}")
    public DemandePrix getDemandePrixById(@PathVariable Long id) {
        return demandePrixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande de prix non trouvée"));
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    public List<DemandePrix> getDemandesPrixByFournisseur(@PathVariable Long fournisseurId) {
        return demandePrixRepository.findByFournisseur_Id(fournisseurId);
    }

    @PostMapping
    public DemandePrix createDemandePrix(@RequestBody DemandePrix demandePrix) {
        // ✅ Associer les lignes avant de sauvegarder
        if (demandePrix.getLignes() != null) {
            demandePrix.getLignes().forEach(l -> l.setDemandePrix(demandePrix));
        }
        return demandePrixRepository.save(demandePrix);
    }
    @PutMapping("/{id}")
    public DemandePrix updateDemandePrix(@PathVariable Long id, @RequestBody DemandePrix demandePrix) {
        DemandePrix existing = demandePrixRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande de prix non trouvée"));
        
        existing.setFournisseur(demandePrix.getFournisseur());
        existing.setDateDemande(demandePrix.getDateDemande());
        existing.setStatut(demandePrix.getStatut());
        existing.setObservation(demandePrix.getObservation());
        
        return demandePrixRepository.save(existing);
    }
    
    
    

    @DeleteMapping("/{id}")
    public void deleteDemandePrix(@PathVariable Long id) {
        demandePrixRepository.deleteById(id);
    }
}