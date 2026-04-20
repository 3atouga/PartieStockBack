package com.backoffice.atelier.services;

import com.backoffice.atelier.entities.PieceDetachee;
import com.backoffice.atelier.repositories.PieceDetacheeRepository;
import com.backoffice.atelier.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PieceDetacheeService {

    private final PieceDetacheeRepository pieceDetacheeRepository;
    private final StockRepository stockRepository;

    @Transactional
    public PieceDetachee ajouterPieceEquivalente(Long pieceId, Long pieceEquivalenteId) {
        PieceDetachee piece = pieceDetacheeRepository.findById(pieceId)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée: " + pieceId));

        PieceDetachee pieceEquivalente = pieceDetacheeRepository.findById(pieceEquivalenteId)
                .orElseThrow(() -> new RuntimeException("Pièce équivalente non trouvée: " + pieceEquivalenteId));

        piece.getPiecesEquivalentes().add(pieceEquivalente);
        pieceEquivalente.getPiecesEquivalentes().add(piece);

        pieceDetacheeRepository.save(piece);
        pieceDetacheeRepository.save(pieceEquivalente);

        return piece;
    }

    @Transactional
    public PieceDetachee retirerPieceEquivalente(Long pieceId, Long pieceEquivalenteId) {
        PieceDetachee piece = pieceDetacheeRepository.findById(pieceId)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée: " + pieceId));

        PieceDetachee pieceEquivalente = pieceDetacheeRepository.findById(pieceEquivalenteId)
                .orElseThrow(() -> new RuntimeException("Pièce équivalente non trouvée: " + pieceEquivalenteId));

        piece.getPiecesEquivalentes().remove(pieceEquivalente);
        pieceEquivalente.getPiecesEquivalentes().remove(piece);

        pieceDetacheeRepository.save(piece);
        pieceDetacheeRepository.save(pieceEquivalente);

        return piece;
    }

    public Set<PieceDetachee> getPiecesEquivalentes(Long pieceId) {
        PieceDetachee piece = pieceDetacheeRepository.findById(pieceId)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée: " + pieceId));
        return piece.getPiecesEquivalentes();
    }

    public List<PieceDetachee> enrichirAvecStocks(List<PieceDetachee> pieces) {
        pieces.forEach(piece -> {
            Integer stockTotal = stockRepository.getTotalStockByPiece(piece.getId());
            piece.setStockTotal(stockTotal != null ? stockTotal : 0);
            piece.setEnStock(piece.getStockTotal() > 0);
        });
        return pieces;
    }

    // ✅ CORRIGÉ : setMarge au lieu de setMargePourcentage
    @Transactional
    public PieceDetachee updateMarge(Long pieceId, Double nouvelleMarge) {
        PieceDetachee piece = pieceDetacheeRepository.findById(pieceId)
                .orElseThrow(() -> new RuntimeException("Pièce non trouvée: " + pieceId));

        piece.setMarge(nouvelleMarge);

        return pieceDetacheeRepository.save(piece);
    }
}