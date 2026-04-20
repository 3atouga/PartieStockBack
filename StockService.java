package com.backoffice.atelier.services;

import com.backoffice.atelier.entities.*;
import com.backoffice.atelier.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;
    private final PieceDetacheeRepository pieceRepository;
    private final MouvementEntreeRepository mouvementEntreeRepository;
    private final MouvementSortieRepository mouvementSortieRepository;
    private final MouvementTransfertRepository mouvementTransfertRepository;

    // ✅ INCRÉMENTER STOCK
    @Transactional
    public void incrementerStock(PieceDetachee piece, Emplacement emplacement,
                                 Integer quantite, String typeDocument, String numeroDocument) {

        Stock stock = stockRepository.findByPieceDetacheeAndEmplacement(piece, emplacement)
                .orElseGet(() -> {
                    Stock newStock = Stock.builder()
                            .pieceDetachee(piece)
                            .emplacement(emplacement)
                            .quantiteDisponible(0)
                            .quantiteReservee(0)
                            .quantiteMinimum(piece.getSeuilAlerte() != null ? piece.getSeuilAlerte() : 5)
                            .build();
                    return stockRepository.save(newStock);
                });

        stock.setQuantiteDisponible(stock.getQuantiteDisponible() + quantite);
        stock.setDernierDocumentType(typeDocument);
        stock.setDernierDocumentNumero(numeroDocument);
        stock.setDateDerniereMaj(LocalDateTime.now());
        stockRepository.save(stock);

        log.info("Stock incrémenté: {} +{} → {} ({})",
                piece.getReference(), quantite, stock.getQuantiteDisponible(), emplacement);
    }

    // ✅ DÉCRÉMENTER STOCK
    @Transactional
    public void decrementerStock(PieceDetachee piece, Emplacement emplacement,
                                 Integer quantite, String typeDocument, String numeroDocument) {

        Stock stock = stockRepository.findByPieceDetacheeAndEmplacement(piece, emplacement)
                .orElseThrow(() -> new RuntimeException(
                        "Stock introuvable pour la pièce " + piece.getReference()
                        + " à l'emplacement " + emplacement));

        if (stock.getQuantiteDisponible() < quantite) {
            throw new RuntimeException(
                    "Stock insuffisant pour " + piece.getReference()
                    + ": disponible=" + stock.getQuantiteDisponible()
                    + ", demandé=" + quantite);
        }

        stock.setQuantiteDisponible(stock.getQuantiteDisponible() - quantite);
        stock.setDernierDocumentType(typeDocument);
        stock.setDernierDocumentNumero(numeroDocument);
        stock.setDateDerniereMaj(LocalDateTime.now());
        stockRepository.save(stock);

        log.info("Stock décrémenté: {} -{} → {} ({})",
                piece.getReference(), quantite, stock.getQuantiteDisponible(), emplacement);
    }

    // ✅ TRANSFÉRER STOCK MAGASIN → ATELIER
    @Transactional
    public void transfererStock(Long pieceId, Integer quantite,
                                Emplacement source, Emplacement destination,
                                String numeroDocument) {

        PieceDetachee piece = pieceRepository.findById(pieceId)
                .orElseThrow(() -> new RuntimeException("Pièce introuvable: " + pieceId));

        // Décrémenter source
        decrementerStock(piece, source, quantite, "TRANSFERT", numeroDocument);

        // Incrémenter destination
        incrementerStock(piece, destination, quantite, "TRANSFERT", numeroDocument);

        // ✅ Créer le mouvement de transfert
        MouvementTransfert mouvement = new MouvementTransfert();
        mouvement.setPiece(piece);
        mouvement.setQuantiteMouvement(quantite);
        mouvement.setTypeMouvement(TypeOperation.TRANSFERT_INTERNE);
        mouvement.setEmplacementSource(source);
        mouvement.setEmplacementDestination(destination);
        mouvement.setDateMouvement(LocalDate.now());
        mouvementTransfertRepository.save(mouvement);

        log.info("Transfert effectué: {} x{} de {} vers {}", 
                piece.getReference(), quantite, source, destination);
    }

    // ✅ CRÉER MOUVEMENT D'ENTRÉE (lors validation BR/FA)
    @Transactional
    public void creerMouvementEntree(PieceDetachee piece, Integer quantite,
                                     BonReception bonReception) {

        MouvementEntree mouvement = new MouvementEntree();
        mouvement.setPiece(piece);
        mouvement.setQuantiteMouvement(quantite);
        mouvement.setTypeMouvement(TypeOperation.ENTREE_FOURNISSEUR);
        mouvement.setDateMouvement(LocalDate.now());
        mouvement.setBonReception(bonReception);
        mouvementEntreeRepository.save(mouvement);

        log.info("Mouvement entrée créé: {} x{} (BR: {})",
                piece.getReference(), quantite, bonReception.getNumero());
    }

    // ✅ CRÉER MOUVEMENT DE SORTIE (lors validation FV)
    @Transactional
    public void creerMouvementSortie(PieceDetachee piece, Integer quantite,
                                     FactureVente factureVente) {

        MouvementSortie mouvement = new MouvementSortie();
        mouvement.setPiece(piece);
        mouvement.setQuantiteMouvement(quantite);
        mouvement.setTypeMouvement(TypeOperation.SORTIE_CLIENT);
        mouvement.setDateMouvement(LocalDate.now());
        mouvement.setFactureVente(factureVente);
        mouvementSortieRepository.save(mouvement);

        log.info("Mouvement sortie créé: {} x{} (FV: {})",
                piece.getReference(), quantite, factureVente.getNumero());
    }
}