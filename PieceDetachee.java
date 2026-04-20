package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pieces_detachees")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PieceDetachee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String designation;

    @Column(unique = true, nullable = false, length = 50)
    private String reference;

    @Column(length = 50)
    private String referenceOrigine;

    // ✅ TYPE ORIGINE calculé automatiquement
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOrigine typeOrigine = TypeOrigine.ORIGINE;

    @Column(nullable = false)
    private Double prixAchat;

    // ✅ Garde le nom de colonne existant dans la BD
    @Column(name = "marge_pourcentage", nullable = false)
    private Double marge = 25.0;

    // ✅ Seuil alerte - nouvelle colonne avec valeur par défaut
    @Column(columnDefinition = "integer default 5")
    private Integer seuilAlerte = 5;

    // ✅ Prix vente calculé à la volée (non stocké)
    @Transient
    public Double getPrixVente() {
        if (prixAchat != null && marge != null) {
            return prixAchat * (1 + marge / 100);
        }
        return prixAchat;
    }

    // ✅ Stocks dérivés
    @Transient
    private Integer stockMagasin;

    @Transient
    private Integer stockAtelier;

    @Transient
    private Integer stockTotal;

    @Transient
    private boolean enStock;

    // ✅ Colonnes existantes dans ta BD
    private String description;
    private String imageUrl;
    private String unite;

    // ✅ Catégorie
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    // ✅ Famille conservée
    @ManyToOne
    @JoinColumn(name = "famille_id")
    private Famille famille;

    // ✅ Sous-famille conservée
    @ManyToOne
    @JoinColumn(name = "sous_famille_id")
    private SousFamille sousFamille;

    // ✅ Fournisseur principal
    @ManyToOne
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    // ✅ Pièces équivalentes
    @ManyToMany
    @JoinTable(
        name = "pieces_equivalentes",
        joinColumns = @JoinColumn(name = "piece_id"),
        inverseJoinColumns = @JoinColumn(name = "piece_equivalente_id")
    )
    @JsonIgnore
    @Builder.Default
    private Set<PieceDetachee> piecesEquivalentes = new HashSet<>();

    private boolean actif = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        detecterTypeOrigine();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        detecterTypeOrigine();
    }

    // ✅ LOGIQUE TYPE ORIGINE
    // reference == referenceOrigine ou referenceOrigine null → ORIGINE
    // reference commence par "SADP-" → SEMI_ADAPTABLE
    // sinon → ADAPTABLE
    private void detecterTypeOrigine() {
        if (referenceOrigine == null || referenceOrigine.isBlank()) {
            this.typeOrigine = TypeOrigine.ORIGINE;
            return;
        }
        if (this.reference.equals(this.referenceOrigine)) {
            this.typeOrigine = TypeOrigine.ORIGINE;
        } else if (this.reference.startsWith("SADP-")) {
            this.typeOrigine = TypeOrigine.SEMI_ADAPTABLE;
        } else {
            this.typeOrigine = TypeOrigine.ADAPTABLE;
        }
    }

    public enum TypeOrigine {
        ORIGINE,
        ADAPTABLE,
        SEMI_ADAPTABLE
    }
}