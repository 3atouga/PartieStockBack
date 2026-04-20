package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // Ex: "PIECE_CREATE", "STOCK_READ", "FACTURE_DELETE"

    @Column(nullable = false)
    private String module; // Ex: "Pièces Détachées", "Stock", "Facturation"

    @Column(nullable = false)
    private String action; // Ex: "Créer", "Lire", "Modifier", "Supprimer"

    private String description;

    @Column(nullable = false)
    private boolean actif = true;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
