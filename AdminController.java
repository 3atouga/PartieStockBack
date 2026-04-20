package com.backoffice.atelier.Controller;

import com.backoffice.atelier.DTO.CreateEmployeeRequest;
import com.backoffice.atelier.entities.Utilisateur;
import com.backoffice.atelier.services.AdminService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRATEUR')") // Seulement les admins
public class AdminController {

    private final AdminService adminService;

    /**
     * Créer un compte employé
     */
    @PostMapping("/employees")
    public ResponseEntity<String> createEmployee(@RequestBody CreateEmployeeRequest request) {
        String message = adminService.createEmployee(request);
        return ResponseEntity.ok(message);
    }

    /**
     * Lister tous les utilisateurs
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Utilisateur>> getAllEmployees() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    /**
     * Activer/Désactiver un utilisateur
     */
    @PatchMapping("/employees/{id}/toggle-status")
    public ResponseEntity<String> toggleUserStatus(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.toggleUserStatus(id));
    }

    /**
     * Réinitialiser le mot de passe d'un utilisateur
     */
    @PostMapping("/employees/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Long id) throws MessagingException {
        return ResponseEntity.ok(adminService.resetUserPassword(id));
    }

    /**
     * Mettre à jour un utilisateur
     */
    @PutMapping("/employees/{id}")
    public ResponseEntity<Utilisateur> updateEmployee(
            @PathVariable Long id,
            @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.ok(adminService.updateUser(id, request));
    }

    /**
     * Supprimer un utilisateur
     */
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }
}
