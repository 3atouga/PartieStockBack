package com.backoffice.atelier.entities;

public enum StatutDocument {
    BROUILLON("Brouillon"),
    VALIDE("Validé"),
    ENVOYE("Envoyé"),
    CLOTURE("Clôturé"),
    ANNULE("Annulé");

    private final String displayName;

    StatutDocument(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

