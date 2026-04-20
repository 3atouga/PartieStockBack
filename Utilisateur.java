package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informations personnelles
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hashé

    private String telephone;

    private String photoProfil; // URL ou chemin

    // Informations professionnelles
    private String poste;
    private String specialite;
    private String service;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Statut du compte
    @Column(nullable = false)
    private boolean actif = false; // Inactif par défaut jusqu'à validation admin

    @Column(nullable = false)
    private boolean premierConnexion = true;

    // Code de vérification (si besoin)
    private String verificationCode;
    private LocalDateTime verificationExpireAt;

    // Mot de passe temporaire
    private String tempPassword;
    private LocalDateTime tempPasswordExpireAt;

    // Préférences
    private String langue = "FR"; // FR ou AR

    @Column(nullable = false)
    private boolean notificationsActives = true;

    // Dates d'audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
