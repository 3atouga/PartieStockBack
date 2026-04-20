package com.backoffice.atelier.entities;

public enum RoleType {
    ADMINISTRATEUR("Administrateur", 0),
    DIRECTEUR_TECHNIQUE("Directeur Technique", 1),
    CONSEILLEUR_SERVICE("Conseilleur Service", 2),
    CHEF_ATELIER("Chef Atelier", 2),
    RESPONSABLE_PDR("Responsable PDR", 2),
    TECHNICIEN("Technicien", 3),
    CHEF_EQUIPE("Chef Équipe", 3),
    RESSOURCE_MECANICIEN("Mécanicien", 4),
    RESSOURCE_TOLIER("Tôlier", 4),
    RESSOURCE_AUTRE("Autre Ressource", 4),
    MAGASINIER_MAGASIN("Magasinier Magasin", 5),
    MAGASINIER_ATELIER("Magasinier Atelier", 5);

    private final String displayName;
    private final int hierarchyLevel;

    RoleType(String displayName, int hierarchyLevel) {
        this.displayName = displayName;
        this.hierarchyLevel = hierarchyLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getHierarchyLevel() {
        return hierarchyLevel;
    }
}


