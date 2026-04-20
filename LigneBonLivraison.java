package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lignes_bon_livraison")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneBonLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bon_livraison_id")
    @JsonBackReference
    private BonLivraison bonLivraison;  // ✅ Changé de BonLivraisonSortant

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee piece;

    private Integer quantite;

    private Double prixUnitaire;

    private Double montantLigne;

    @PrePersist
    @PreUpdate
    private void calculerMontant() {
        if (quantite != null && prixUnitaire != null) {
            this.montantLigne = quantite * prixUnitaire;
        }
    }
}