package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"piece_id", "emplacement"})
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "piece_id", nullable = false)
    private PieceDetachee pieceDetachee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Emplacement emplacement;

    @Column(nullable = false)
    private Integer quantiteDisponible = 0;

    @Column(nullable = false)
    private Integer quantiteReservee = 0;

    @Column(nullable = false)
    private Integer quantiteMinimum = 5;

    @Column(length = 100)
    private String dernierDocumentType;

    @Column(length = 50)
    private String dernierDocumentNumero;

    private LocalDateTime dateDerniereMaj;

    @PrePersist
    protected void onCreate() {
        dateDerniereMaj = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dateDerniereMaj = LocalDateTime.now();
    }
}