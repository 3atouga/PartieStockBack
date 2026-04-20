package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lignes_facture_vente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneFactureVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "facture_vente_id")
    @JsonBackReference
    private FactureVente factureVente;

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