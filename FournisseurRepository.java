package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {

    List<Fournisseur> findByActifTrue();

    Optional<Fournisseur> findByCode(String code);

    Optional<Fournisseur> findByMatriculeFiscale(String matriculeFiscale);

    // ✅ VOIRFOURCATEG
    List<Fournisseur> findByCategories_Id(Long categorieId);
}