package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lignes_fa")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneFactureAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "facture_achat_id", nullable = false)
    @JsonBackReference
    private FactureAchat factureAchat;

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee piece;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(name = "montant_ligne")
    private Double montantLigne;

    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    protected void calculerMontant() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (quantite != null && prixUnitaire != null) {
            this.montantLigne = quantite * prixUnitaire;
        }
    }
}