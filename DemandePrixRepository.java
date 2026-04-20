package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.DemandePrix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DemandePrixRepository extends JpaRepository<DemandePrix, Long> {
    
    List<DemandePrix> findByFournisseur_Id(Long fournisseurId);
    
    List<DemandePrix> findByStatut(DemandePrix.StatutDocument statut);
    
    Optional<DemandePrix> findByNumero(String numero);
}