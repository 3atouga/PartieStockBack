package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.LigneAvoirAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneAvoirAchatRepository extends JpaRepository<LigneAvoirAchat, Long> {

    List<LigneAvoirAchat> findByAvoirAchat_Id(Long avoirAchatId);

    List<LigneAvoirAchat> findByPiece_Id(Long pieceId);
}