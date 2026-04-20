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
@Table(name = "bons_livraison")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonLivraison {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDate dateLivraison;

    @Enumerated(EnumType.STRING)
    private StatutBL statut;

    private Double montantHT;
    private Double montantTVA;
    private Double montantTTC;
    private String observation;

    // ✅ UN SEUL CHAMP - nom colonne exact
    @Column(name = "stock_mis_a_jour", nullable = false,
            columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean stockMisAJour = false;

    @OneToMany(mappedBy = "bonLivraison", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<LigneBonLivraison> lignes = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) statut = StatutBL.BROUILLON;
        if (stockMisAJour == null) stockMisAJour = false;
        if (lignes != null) {
            for (LigneBonLivraison ligne : lignes) {
                ligne.setBonLivraison(this);
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (stockMisAJour == null) stockMisAJour = false;
    }

    // ✅ Méthodes explicites pour éviter conflit Lombok/boolean
    public boolean isStockMisAJour() {
        return Boolean.TRUE.equals(stockMisAJour);
    }

    public void setStockMisAJour(boolean value) {
        this.stockMisAJour = value;
    }

    public Boolean getStockMisAJour() {
        return stockMisAJour;
    }

    public enum StatutBL {
        BROUILLON,
        PREPARE,
        EXPEDIE,
        LIVRE,
        ANNULE
    }
}