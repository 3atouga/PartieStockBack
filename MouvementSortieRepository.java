package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.MouvementSortie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouvementSortieRepository extends JpaRepository<MouvementSortie, Long> {

    List<MouvementSortie> findByFactureVente_Id(Long factureVenteId);
}