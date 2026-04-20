package com.backoffice.atelier.entities;

import com.backoffice.atelier.entities.Emplacement;
import com.backoffice.atelier.entities.StatutAlerte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertes_rupture")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlerteRupture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee piece;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Emplacement emplacement;

    @Column(nullable = false)
    private Integer quantiteAuMoment;

    @Column(nullable = false)
    private Integer quantiteMinimum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAlerte statut = StatutAlerte.ACTIVE;

    @Column(nullable = false)
    private LocalDateTime dateAlerte;

    private LocalDateTime dateTraitement;

    @ManyToOne
    @JoinColumn(name = "utilisateur_traitement_id")
    private Utilisateur utilisateurTraitement;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (dateAlerte == null) {
            dateAlerte = LocalDateTime.now();
        }
    }
}