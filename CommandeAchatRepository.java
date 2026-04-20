package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.CommandeAchat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommandeAchatRepository extends JpaRepository<CommandeAchat, Long> {
    
    List<CommandeAchat> findByFournisseur_Id(Long fournisseurId);
    
    List<CommandeAchat> findByStatut(CommandeAchat.StatutCommande statut);
    
    Optional<CommandeAchat> findByNumero(String numero);
    
    List<CommandeAchat> findByDemandePrix_Id(Long demandePrixId);
}