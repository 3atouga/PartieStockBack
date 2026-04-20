package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.MouvementEntree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MouvementEntreeRepository extends JpaRepository<MouvementEntree, Long> {

    List<MouvementEntree> findByBonReception_Id(Long bonReceptionId);
}