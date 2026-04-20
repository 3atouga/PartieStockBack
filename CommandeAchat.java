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
@Table(name = "commandes_achat")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeAchat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "demande_prix_id")
    private DemandePrix demandePrix;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    private LocalDate dateCommande;
    private LocalDate dateArriveePrevue;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    private Double montantHT;
    private Double montantTVA;
    private Double montantTTC;
    private String observation;

    // ✅ LigneCommandeAchat
    @OneToMany(mappedBy = "commandeAchat", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<LigneCommandeAchat> lignes = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) statut = StatutCommande.BROUILLON;
        if (numero == null) numero = "CA-" + System.currentTimeMillis();
        if (lignes != null) {
            for (LigneCommandeAchat ligne : lignes) {
                ligne.setCommandeAchat(this);
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StatutCommande {
        BROUILLON,
        ENVOYEE,
        CONFIRMEE,
        EN_COURS,
        LIVREE,
        ANNULEE
    }
}