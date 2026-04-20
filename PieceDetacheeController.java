package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.PieceDetachee;
import com.backoffice.atelier.repositories.PieceDetacheeRepository;
import com.backoffice.atelier.services.PieceDetacheeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/pieces-detachees")
@RequiredArgsConstructor
public class PieceDetacheeController {

    private final PieceDetacheeRepository pieceDetacheeRepository;
    private final PieceDetacheeService pieceDetacheeService;

    @GetMapping
    public List<PieceDetachee> getAllPieces(
            @RequestParam(required = false) Boolean avecStock
    ) {
        List<PieceDetachee> pieces = pieceDetacheeRepository.findAll();
        if (Boolean.TRUE.equals(avecStock)) {
            return pieceDetacheeService.enrichirAvecStocks(pieces);
        }
        return pieces;
    }

    @GetMapping("/{id}")
    public PieceDetachee getPieceById(@PathVariable Long id) {
        return pieceDetacheeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée"));
    }

    @GetMapping("/categorie/{categorieId}")
    public List<PieceDetachee> getByCategorie(@PathVariable Long categorieId) {
        return pieceDetacheeRepository.findByCategorie_Id(categorieId);
    }

    @GetMapping("/famille/{familleId}")
    public List<PieceDetachee> getByFamille(@PathVariable Long familleId) {
        return pieceDetacheeRepository.findByFamille_Id(familleId);
    }

    @GetMapping("/sous-famille/{sousFamilleId}")
    public List<PieceDetachee> getBySousFamille(@PathVariable Long sousFamilleId) {
        return pieceDetacheeRepository.findBySousFamille_Id(sousFamilleId);
    }

    @GetMapping("/type-origine/{typeOrigine}")
    public List<PieceDetachee> getByTypeOrigine(
            @PathVariable PieceDetachee.TypeOrigine typeOrigine
    ) {
        return pieceDetacheeRepository.findByTypeOrigine(typeOrigine);
    }

    @GetMapping("/{id}/equivalentes")
    public Set<PieceDetachee> getPiecesEquivalentes(@PathVariable Long id) {
        return pieceDetacheeService.getPiecesEquivalentes(id);
    }

    @PostMapping("/{pieceId}/equivalentes/{pieceEquivalenteId}")
    public PieceDetachee ajouterEquivalente(
            @PathVariable Long pieceId,
            @PathVariable Long pieceEquivalenteId
    ) {
        return pieceDetacheeService.ajouterPieceEquivalente(pieceId, pieceEquivalenteId);
    }

    @DeleteMapping("/{pieceId}/equivalentes/{pieceEquivalenteId}")
    public PieceDetachee retirerEquivalente(
            @PathVariable Long pieceId,
            @PathVariable Long pieceEquivalenteId
    ) {
        return pieceDetacheeService.retirerPieceEquivalente(pieceId, pieceEquivalenteId);
    }

    @PatchMapping("/{id}/marge")
    public PieceDetachee updateMarge(
            @PathVariable Long id,
            @RequestParam Double marge
    ) {
        return pieceDetacheeService.updateMarge(id, marge);
    }

    @PostMapping
    public PieceDetachee createPiece(@RequestBody PieceDetachee piece) {
        return pieceDetacheeRepository.save(piece);
    }

    @PutMapping("/{id}")
    public PieceDetachee updatePiece(
            @PathVariable Long id,
            @RequestBody PieceDetachee piece
    ) {
        PieceDetachee existing = pieceDetacheeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée"));

        existing.setDesignation(piece.getDesignation());
        existing.setReference(piece.getReference());
        existing.setReferenceOrigine(piece.getReferenceOrigine());
        existing.setPrixAchat(piece.getPrixAchat());
        existing.setMarge(piece.getMarge());
        existing.setSeuilAlerte(piece.getSeuilAlerte());
        existing.setDescription(piece.getDescription());
        existing.setUnite(piece.getUnite());
        existing.setCategorie(piece.getCategorie());
        existing.setFamille(piece.getFamille());
        existing.setSousFamille(piece.getSousFamille());
        existing.setFournisseur(piece.getFournisseur());
        existing.setActif(piece.isActif());

        return pieceDetacheeRepository.save(existing);
    }

    // ✅ DELETE avec fallback désactivation
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePiece(@PathVariable Long id) {
        try {
            // ✅ Tenter suppression directe
            pieceDetacheeRepository.deleteById(id);
            return ResponseEntity.ok("Pièce supprimée avec succès");

        } catch (Exception e) {
            // ✅ Si erreur FK → désactiver au lieu de supprimer
            try {
                PieceDetachee piece = pieceDetacheeRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pièce non trouvée"));
                piece.setActif(false);
                pieceDetacheeRepository.save(piece);
                return ResponseEntity.ok(
                    "Pièce désactivée (liée à des stocks ou documents existants)"
                );
            } catch (Exception ex) {
                return ResponseEntity.badRequest()
                        .body("Impossible de supprimer ou désactiver: " + ex.getMessage());
            }
        }
    }
}