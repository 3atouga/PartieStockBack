package com.backoffice.atelier.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mouvements_transfert")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MouvementTransfert extends MouvementStock {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Emplacement emplacementSource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Emplacement emplacementDestination;
}