package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mouvements_sortie")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementSortie extends MouvementStock {

    @ManyToOne
    @JoinColumn(name = "facture_vente_id", nullable = false)
    private FactureVente factureVente;
}