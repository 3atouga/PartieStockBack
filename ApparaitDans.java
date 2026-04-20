package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "apparait_dans",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"demande_prix_id", "piece_id", "commande_achat_id"})
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApparaitDans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Ligne de demande de prix
    @ManyToOne
    @JoinColumn(name = "demande_prix_id", nullable = false)
    private DemandePrix demandePrix;

    // ✅ Pièce concernée
    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee piece;

    // ✅ Commande d'achat résultante
    @ManyToOne
    @JoinColumn(name = "commande_achat_id", nullable = false)
    private CommandeAchat commandeAchat;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}