package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    
    List<Categorie> findByActifTrue();
}