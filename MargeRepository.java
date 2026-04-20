package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.Marge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MargeRepository extends JpaRepository<Marge, Long> {
    
    List<Marge> findByActifTrue();
    
    Optional<Marge> findByNiveauAndActifTrue(Marge.NiveauMarge niveau);
    
    Optional<Marge> findByNiveauAndReferenceIdAndActifTrue(
        Marge.NiveauMarge niveau, 
        Long referenceId
    );
    
    @Query("SELECT m FROM Marge m WHERE m.niveau = 'GLOBAL' AND m.actif = true ORDER BY m.createdAt DESC")
    Optional<Marge> findMargeGlobaleActive();
}