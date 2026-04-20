package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.Famille;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamilleRepository extends JpaRepository<Famille, Long> {
    
    List<Famille> findByActifTrue();
    
    Optional<Famille> findByCode(String code);
}