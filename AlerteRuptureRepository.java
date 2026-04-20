package com.backoffice.atelier.repositories;

import com.backoffice.atelier.entities.AlerteRupture;
import com.backoffice.atelier.entities.PieceDetachee;
import com.backoffice.atelier.entities.Emplacement;
import com.backoffice.atelier.entities.StatutAlerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlerteRuptureRepository extends JpaRepository<AlerteRupture, Long> {
    List<AlerteRupture> findByStatut(StatutAlerte statut);
    List<AlerteRupture> findByPieceAndEmplacement(PieceDetachee piece, Emplacement emplacement);
    List<AlerteRupture> findByStatutOrderByDateAlerteDesc(StatutAlerte statut);
}