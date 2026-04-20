package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lignes_ca")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_achat_id", nullable = false)
    @JsonBackReference
    private CommandeAchat commandeAchat;

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee piece;

    @Column(name = "quantite_demandee", nullable = false)
    private Integer quantiteDemandee;

    @Column(name = "quantite_commandee", nullable = false)
    private Integer quantiteCommandee;

    @Column(nullable = false)
    private Double prixUnitaire;

    @Column(name = "montant_ligne")
    private Double montantLigne;

    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    protected void calculerMontant() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (quantiteCommandee == null && quantiteDemandee != null) {
            quantiteCommandee = quantiteDemandee;
        }
        if (quantiteDemandee == null && quantiteCommandee != null) {
            quantiteDemandee = quantiteCommandee;
        }
        if (quantiteCommandee != null && prixUnitaire != null) {
            this.montantLigne = quantiteCommandee * prixUnitaire;
        }
    }
}