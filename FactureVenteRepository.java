package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.FactureVente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactureVenteRepository extends JpaRepository<FactureVente, Long> {

    Optional<FactureVente> findByNumero(String numero);
}