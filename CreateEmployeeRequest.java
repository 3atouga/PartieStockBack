package com.backoffice.atelier.DTO;

import lombok.Data;

@Data
public class CreateEmployeeRequest {
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String poste;
    private String specialite;
    private String service;
    private Long roleId;
    private String langue; // "FR" ou "AR"
}