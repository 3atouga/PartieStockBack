package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.FactureAchat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FactureAchatRepository extends JpaRepository<FactureAchat, Long> {
    
    List<FactureAchat> findByFournisseur_Id(Long fournisseurId);
    
    List<FactureAchat> findByStatut(FactureAchat.StatutFacture statut);
    
    Optional<FactureAchat> findByNumero(String numero);
    
    List<FactureAchat> findByCommandeAchat_Id(Long commandeAchatId);
    
    @Query("SELECT f FROM FactureAchat f ORDER BY f.dateFacture DESC")
    List<FactureAchat> findAllOrderByDateDesc();
}