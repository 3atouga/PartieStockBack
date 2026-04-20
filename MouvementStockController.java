package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.*;
import com.backoffice.atelier.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mouvements-stock")
@RequiredArgsConstructor
public class MouvementStockController {

    private final MouvementStockRepository mouvementStockRepository;
    private final MouvementEntreeRepository mouvementEntreeRepository;
    private final MouvementSortieRepository mouvementSortieRepository;
    private final MouvementTransfertRepository mouvementTransfertRepository;

    // ✅ Tous les mouvements
    @GetMapping
    public List<MouvementStock> getAll() {
        return mouvementStockRepository.findAll();
    }

    // ✅ Par pièce
    @GetMapping("/piece/{pieceId}")
    public List<MouvementStock> getByPiece(@PathVariable Long pieceId) {
        return mouvementStockRepository.findByPiece_IdOrderByDateMouvementDesc(pieceId);
    }

    // ✅ Entrées seulement
    @GetMapping("/entrees")
    public List<MouvementEntree> getEntrees() {
        return mouvementEntreeRepository.findAll();
    }

    // ✅ Sorties seulement
    @GetMapping("/sorties")
    public List<MouvementSortie> getSorties() {
        return mouvementSortieRepository.findAll();
    }

    // ✅ Transferts seulement
    @GetMapping("/transferts")
    public List<MouvementTransfert> getTransferts() {
        return mouvementTransfertRepository.findAll();
    }

    // ✅ Créer une entrée manuellement
    @PostMapping("/entrees")
    public MouvementEntree createEntree(@RequestBody MouvementEntree entree) {
        return mouvementEntreeRepository.save(entree);
    }

    // ✅ Créer un transfert manuellement
    @PostMapping("/transferts")
    public MouvementTransfert createTransfert(@RequestBody MouvementTransfert transfert) {
        return mouvementTransfertRepository.save(transfert);
    }
}