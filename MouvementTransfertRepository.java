package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.MouvementTransfert;
import com.backoffice.atelier.entities.Emplacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouvementTransfertRepository extends JpaRepository<MouvementTransfert, Long> {

    List<MouvementTransfert> findByEmplacementSource(Emplacement source);

    List<MouvementTransfert> findByEmplacementDestination(Emplacement destination);
}