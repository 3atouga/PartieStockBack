package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.MouvementStock;
import com.backoffice.atelier.entities.TypeOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    List<MouvementStock> findByPiece_Id(Long pieceId);

    List<MouvementStock> findByTypeMouvement(TypeOperation type);

    List<MouvementStock> findByDateMouvementBetween(LocalDate debut, LocalDate fin);

    List<MouvementStock> findByPiece_IdOrderByDateMouvementDesc(Long pieceId);
}