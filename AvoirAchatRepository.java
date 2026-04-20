package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.AvoirAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvoirAchatRepository extends JpaRepository<AvoirAchat, Long> {

    Optional<AvoirAchat> findByNumero(String numero);

    List<AvoirAchat> findByFournisseur_Id(Long fournisseurId);

    List<AvoirAchat> findByTypeAvoir(AvoirAchat.StatutAvoir statut);

    List<AvoirAchat> findByFactureAchat_Id(Long factureAchatId);
}