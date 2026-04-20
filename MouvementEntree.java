package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mouvements_entree")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementEntree extends MouvementStock {

    @ManyToOne
    @JoinColumn(name = "bon_reception_id", nullable = false)
    private BonReception bonReception;
}