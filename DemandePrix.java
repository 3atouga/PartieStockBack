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
@Table(name = "demandes_prix")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandePrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    private LocalDate dateDemande;

    @Enumerated(EnumType.STRING)
    private StatutDocument statut;

    private Double montantTotal;

    private String observation;

    @OneToMany(mappedBy = "demandePrix", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<LigneDemandePrix> lignes = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ MÉTHODE HELPER POUR AJOUTER DES LIGNES
    public void addLigne(LigneDemandePrix ligne) {
        lignes.add(ligne);
        ligne.setDemandePrix(this);
    }

    public void removeLigne(LigneDemandePrix ligne) {
        lignes.remove(ligne);
        ligne.setDemandePrix(null);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) {
            statut = StatutDocument.BROUILLON;
        }
        calculerMontantTotal();
        
        // ✅ ASSOCIER AUTOMATIQUEMENT LES LIGNES
        if (lignes != null) {
            for (LigneDemandePrix ligne : lignes) {
                ligne.setDemandePrix(this);
            }
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculerMontantTotal();
    }

    private void calculerMontantTotal() {
        if (lignes != null && !lignes.isEmpty()) {
            montantTotal = lignes.stream()
                    .mapToDouble(ligne -> ligne.getMontantLigne() != null ? ligne.getMontantLigne() : 0.0)
                    .sum();
        } else {
            montantTotal = 0.0;
        }
    }

    public enum StatutDocument {
        BROUILLON,
        ENVOYEE,
        RECUE,
        VALIDEE,
        ANNULEE
    }
}