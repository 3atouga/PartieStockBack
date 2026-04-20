package com.backoffice.atelier.entities;

public enum TypeAvoir {
    ERREUR_PRIX("Erreur de Prix"),
    ERREUR_QUANTITE("Erreur de Quantité"),
    PRODUIT_DEFECTUEUX("Produit Défectueux"),
    RETOUR_CLIENT("Retour Client");

    private final String displayName;

    TypeAvoir(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}