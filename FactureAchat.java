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
@Table(name = "factures_achat")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactureAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "commande_achat_id")
    private CommandeAchat commandeAchat;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    private LocalDate dateFacture;
    private LocalDate dateEcheance;

    @Enumerated(EnumType.STRING)
    private StatutFacture statut;

    private Double montantHT;
    private Double montantTVA;
    private Double montantTTC;
    private String observation;

    // ✅ UN SEUL CHAMP - nom colonne exact
    @Column(name = "stock_mis_a_jour", nullable = false,
            columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean stockMisAJour = false;

    @OneToMany(mappedBy = "factureAchat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<LigneFactureAchat> lignes = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) statut = StatutFacture.EN_ATTENTE;
        if (numero == null) numero = "FA-" + System.currentTimeMillis();
        if (stockMisAJour == null) stockMisAJour = false;
        if (lignes != null) {
            for (LigneFactureAchat ligne : lignes) {
                ligne.setFactureAchat(this);
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

    public enum StatutFacture {
        EN_ATTENTE,
        VALIDEE,
        PAYEE,
        ANNULEE
    }
}