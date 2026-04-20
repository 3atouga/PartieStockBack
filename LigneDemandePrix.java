package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lignes_demande_prix")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneDemandePrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "demande_prix_id", nullable = false)
    @JsonBackReference
    private DemandePrix demandePrix;

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee piece;

    private Integer quantiteDemandee;

    private Double prixUnitaire;

    private Double montantLigne;

    @PrePersist
    @PreUpdate
    protected void calculerMontant() {
        if (quantiteDemandee != null && prixUnitaire != null) {
            montantLigne = quantiteDemandee * prixUnitaire;
        }
    }
}	