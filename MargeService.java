package com.backoffice.atelier.services;

import com.backoffice.atelier.entities.Marge;
import com.backoffice.atelier.repositories.MargeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MargeService {

    private final MargeRepository margeRepository;

    public double getMargeGlobale() {
        Optional<Marge> marge = margeRepository.findByNiveauAndActifTrue(Marge.NiveauMarge.GLOBAL);
        return marge.map(Marge::getTauxPourcentage).orElse(0.0);
    }

    public double getMargeCategorie(Long categorieId) {
        Optional<Marge> marge = margeRepository.findByNiveauAndReferenceIdAndActifTrue(
            Marge.NiveauMarge.CATEGORIE, 
            categorieId
        );
        return marge.map(Marge::getTauxPourcentage).orElse(getMargeGlobale());
    }

    public double getMargePiece(Long pieceId) {
        Optional<Marge> marge = margeRepository.findByNiveauAndReferenceIdAndActifTrue(
            Marge.NiveauMarge.PIECE, 
            pieceId
        );
        return marge.map(Marge::getTauxPourcentage).orElse(0.0);
    }

    public double calculerPrixVente(Long pieceId, Long categorieId, double prixAchat) {
        double margePiece = getMargePiece(pieceId);
        if (margePiece > 0) {
            return prixAchat * (1 + margePiece / 100);
        }
        
        double margeCategorie = getMargeCategorie(categorieId);
        if (margeCategorie > 0) {
            return prixAchat * (1 + margeCategorie / 100);
        }
        
        Optional<Marge> margeGlobale = margeRepository.findMargeGlobaleActive();
        double tauxGlobal = margeGlobale.map(Marge::getTauxPourcentage).orElse(25.0);
        
        return prixAchat * (1 + tauxGlobal / 100);
    }
}