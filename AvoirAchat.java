package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "avoirs_achat")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvoirAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @ManyToOne
    @JoinColumn(name = "facture_achat_id")
    private FactureAchat factureAchat;

    private LocalDate dateAvoir;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeAvoir typeAvoir;

    @Column(columnDefinition = "TEXT")
    private String motif;

    private Double montantHT;
    private Double montantTVA;
    private Double montantTTC;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAvoir statut = StatutAvoir.BROUILLON;

    // ✅ LIGNES AVOIR
    @OneToMany(mappedBy = "avoirAchat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<LigneAvoirAchat> lignes = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) statut = StatutAvoir.BROUILLON;
        if (numero == null) numero = "AA-" + System.currentTimeMillis();
        calculerTotaux();
        associerLignes();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculerTotaux();
    }

    private void calculerTotaux() {
        if (lignes != null && !lignes.isEmpty()) {
            double ht = lignes.stream()
                    .mapToDouble(l -> l.getMontantLigne() != null ? l.getMontantLigne() : 0.0)
                    .sum();
            this.montantHT = ht;
            this.montantTVA = ht * 0.19;
            this.montantTTC = ht + this.montantTVA;
        }
    }

    private void associerLignes() {
        if (lignes != null) {
            for (LigneAvoirAchat ligne : lignes) {
                ligne.setAvoirAchat(this);
            }
        }
    }

    public enum StatutAvoir {
        BROUILLON,
        VALIDE,
        ANNULE
    }
}