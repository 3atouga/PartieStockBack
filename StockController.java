package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.Emplacement;
import com.backoffice.atelier.entities.Stock;
import com.backoffice.atelier.repositories.StockRepository;
import com.backoffice.atelier.services.StockService;  // ✅ AJOUTER CETTE LIGNE
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;  // ✅ AJOUTER CETTE LIGNE

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockRepository stockRepository;
    private final StockService stockService;  // ✅ AJOUTER CETTE LIGNE

    @GetMapping
    public List<Stock> getStocksAvecFiltres(
            @RequestParam(required = false) Emplacement emplacement,
            @RequestParam(required = false) Boolean enRupture
    ) {
        return stockRepository.findStocksAvecFiltres(emplacement, enRupture);
    }

    @GetMapping("/{id}")
    public Stock getStockById(@PathVariable Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock non trouvé"));
    }

    @GetMapping("/piece/{pieceId}")
    public List<Stock> getStocksByPiece(@PathVariable Long pieceId) {
        return stockRepository.findByPieceDetachee_Id(pieceId);
    }
    
 // Lister les transferts (mouvements de type TRANSFERT)
    @GetMapping("/transferts")
    public List<Stock> getTransferts() {
        return stockRepository.findAll(); // Remplace par ta logique
    }

    // Lister les alertes de rupture
    @GetMapping("/alertes")
    public List<Stock> getAlertes() {
        return stockRepository.findStocksAvecFiltres(null, true); // enRupture = true
    }

    @GetMapping("/total/{pieceId}")
    public Integer getStockTotal(@PathVariable Long pieceId) {
        return stockRepository.getTotalStockByPiece(pieceId);
    }

    // ✅ ✅ ✅ NOUVELLE MÉTHODE - AJOUTER TOUT ÇA ✅ ✅ ✅
    @PostMapping("/transfert")
    public Map<String, Object> transfererStock(@RequestBody TransfertRequest request) {
        
        stockService.transfererStock(
            request.getPieceId(),
            request.getQuantite(),
            request.getEmplacementSource(),
            request.getEmplacementDestination(),
            request.getNumeroDocument()
        );

        return Map.of(
            "success", true,
            "message", "Transfert effectué avec succès",
            "pieceId", request.getPieceId(),
            "quantite", request.getQuantite(),
            "source", request.getEmplacementSource(),
            "destination", request.getEmplacementDestination()
        );
    }
    
    

    // ✅ DTO pour le transfert
    @lombok.Data
    public static class TransfertRequest {
        private Long pieceId;
        private Integer quantite;
        private Emplacement emplacementSource;
        private Emplacement emplacementDestination;
        private String numeroDocument;
    }
}