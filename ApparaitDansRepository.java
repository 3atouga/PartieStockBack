package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.ApparaitDans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApparaitDansRepository extends JpaRepository<ApparaitDans, Long> {

    // ✅ Toutes les CA liées à une DP
    List<ApparaitDans> findByDemandePrix_Id(Long demandePrixId);

    // ✅ Toutes les DP liées à une CA
    List<ApparaitDans> findByCommandeAchat_Id(Long commandeAchatId);

    // ✅ Vérifier si une pièce d'une DP est déjà dans une CA
    boolean existsByDemandePrix_IdAndPiece_Id(Long demandePrixId, Long pieceId);

    // ✅ Par pièce
    List<ApparaitDans> findByPiece_Id(Long pieceId);
}