package com.backoffice.atelier.entities;

public enum TypeOperation {
    ENTREE_FOURNISSEUR("Entrée Fournisseur"),
    SORTIE_CLIENT("Sortie Client"),
    TRANSFERT_INTERNE("Transfert Interne"),
    CONSOMMATION_ATELIER("Consommation Atelier"),
    RETOUR_CLIENT("Retour Client"),
    AJUSTEMENT_INVENTAIRE("Ajustement Inventaire");

    private final String displayName;

    TypeOperation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}