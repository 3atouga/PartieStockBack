package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.BonReception;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BonReceptionRepository extends JpaRepository<BonReception, Long> {

    Optional<BonReception> findByNumero(String numero);

    List<BonReception> findByCommandeAchat_Id(Long commandeAchatId);

    List<BonReception> findByConformeTrue();

    List<BonReception> findByConformeFalse();
}