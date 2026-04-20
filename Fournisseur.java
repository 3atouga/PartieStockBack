package com.backoffice.atelier.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "fournisseurs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String code;

    @Column(nullable = false)
    private String nom;

    @Column(unique = true, nullable = false, length = 20)
    private String matriculeFiscale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeFournisseur type = TypeFournisseur.EXTERNE;

    private String email;
    private String telephone;
    private String adresse;

    private Integer delaiLivraisonJours = 7;
    private Integer joursEcheance = 30;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MethodeReglement methodeReglement = MethodeReglement.EFFET;

    private Integer noteFournisseur;

    @Column(nullable = false)
    private boolean actif = true;

    // ✅ VOIRFOURCATEG : Un fournisseur peut fournir plusieurs catégories
    @ManyToMany
    @JoinTable(
        name = "voir_four_categ",
        joinColumns = @JoinColumn(name = "id_four"),
        inverseJoinColumns = @JoinColumn(name = "code_cat")
    )
    @Builder.Default
    private Set<Categorie> categories = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public enum TypeFournisseur { INTERNE, EXTERNE }

    public enum MethodeReglement {
        EFFET, ESPECES, CHEQUE, VIREMENT, TRAITE, LETTRE_CHANGE
    }
}