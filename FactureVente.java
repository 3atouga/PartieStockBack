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
@Table(name = "factures_vente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FactureVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "bon_livraison_id")
    private BonLivraison bonLivraison;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private LocalDate dateFacture;
    private LocalDate dateEcheance;

    @Enumerated(EnumType.STRING)
    private StatutFacture statut;

    private Double montantHT;
    private Double montantTVA;
    private Double montantTTC;
    private String observation;

    @OneToMany(mappedBy = "factureVente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<LigneFactureVente> lignes = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) statut = StatutFacture.BROUILLON;
        if (numero == null) numero = "FV-" + System.currentTimeMillis();
        if (lignes != null) {
            for (LigneFactureVente ligne : lignes) {
                ligne.setFactureVente(this);
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StatutFacture {
        BROUILLON,
        VALIDEE,
        PAYEE,
        ANNULEE
    }
}