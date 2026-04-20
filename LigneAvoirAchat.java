package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lignes_avoir_achat")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneAvoirAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "avoir_achat_id", nullable = false)
    @JsonBackReference
    private AvoirAchat avoirAchat;

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee piece;

    @Column(nullable = false)
    private Integer quantiteAvoir;

    @Column(nullable = false)
    private Double prixAvoir;

    private Double montantLigne;

    @PrePersist
    @PreUpdate
    protected void calculerMontant() {
        if (quantiteAvoir != null && prixAvoir != null) {
            this.montantLigne = quantiteAvoir * prixAvoir;
        }
    }
}