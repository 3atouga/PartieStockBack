package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.BonLivraison;  // ✅ Changé
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BonLivraisonRepository extends JpaRepository<BonLivraison, Long> {

    Optional<BonLivraison> findByNumero(String numero);
}