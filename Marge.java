package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "marges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Marge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauMarge niveau;

    private Long referenceId;

    @Column(nullable = false)
    private Double tauxPourcentage;

    @Column(nullable = false)
    private boolean actif = true;

    @ManyToOne
    @JoinColumn(name = "createur_id")
    private Utilisateur createur;

    private LocalDateTime dateApplication;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dateApplication == null) {
            dateApplication = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum NiveauMarge {
        GLOBAL("Marge Globale"),
        CATEGORIE("Marge par Catégorie"),
        PIECE("Marge par Pièce");

        private final String displayName;

        NiveauMarge(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public Double calculerPrixVente(Double prixAchat) {
        return prixAchat * (1 + (tauxPourcentage / 100));
    }
}