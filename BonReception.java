package com.backoffice.atelier.entities;

import com.backoffice.atelier.entities.StatutDocument;
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
@Table(name = "bons_reception")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonReception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "commande_achat_id", nullable = false)
    private CommandeAchat commandeAchat;

    @Column(nullable = false)
    private LocalDate dateReception;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutDocument statut = StatutDocument.BROUILLON;

    @OneToMany(mappedBy = "bonReception", cascade = CascadeType.ALL)
    @Builder.Default
    private List<LigneBR> lignes = new ArrayList<>();

    @Column(nullable = false)
    private boolean conforme = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}