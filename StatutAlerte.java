package com.backoffice.atelier.entities;

public enum StatutAlerte {
    ACTIVE("Active"),
    TRAITEE("Traitée"),
    IGNOREE("Ignorée");

    private final String displayName;

    StatutAlerte(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}