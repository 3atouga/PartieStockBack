package com.backoffice.atelier.services;

import com.backoffice.atelier.DTO.CreateEmployeeRequest;
import com.backoffice.atelier.entities.Role;
import com.backoffice.atelier.entities.Utilisateur;
import com.backoffice.atelier.repositories.RoleRepository;
import com.backoffice.atelier.repositories.UtilisateurRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Créer un compte employé (réservé à l'Admin)
     */
    @Transactional
    public String createEmployee(CreateEmployeeRequest request) {

        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Cet email est déjà utilisé.");
        }

        // Récupérer le rôle
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new IllegalStateException("Rôle introuvable."));

        // Générer un mot de passe temporaire
        String tempPassword = generateTempPassword();

        // Créer l'utilisateur
        Utilisateur user = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .telephone(request.getTelephone())
                .poste(request.getPoste())
                .specialite(request.getSpecialite())
                .service(request.getService())
                .role(role)
                .password(passwordEncoder.encode(tempPassword))
                .actif(true) // Compte activé directement par l'admin
                .premierConnexion(true)
                .langue(request.getLangue() != null ? request.getLangue() : "FR")
                .build();

        utilisateurRepository.save(user);

        // Envoyer l'email de bienvenue avec le mot de passe temporaire
        try {
            emailService.sendWelcomeEmployeeEmail(
                    user.getEmail(),
                    user.getPrenom(),
                    tempPassword
            );
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Compte créé mais erreur lors de l'envoi de l'email.";
        }

        return "Compte employé créé avec succès. Email de bienvenue envoyé.";
    }

    /**
     * Lister tous les utilisateurs
     */
    public List<Utilisateur> getAllUsers() {
        return utilisateurRepository.findAll();
    }

    /**
     * Activer/Désactiver un utilisateur
     */
    @Transactional
    public String toggleUserStatus(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));

        user.setActif(!user.isActif());
        utilisateurRepository.save(user);

        return user.isActif() ? "Utilisateur activé." : "Utilisateur désactivé.";
    }

    /**
     * Réinitialiser le mot de passe d'un utilisateur
     */
    @Transactional
    public String resetUserPassword(Long userId) throws MessagingException {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));

        String tempPassword = generateTempPassword();

        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setPremierConnexion(true);
        utilisateurRepository.save(user);

        emailService.sendTempPasswordHtml(
                user.getEmail(),
                user.getPrenom(),
                tempPassword
        );

        return "Mot de passe réinitialisé. Email envoyé à l'utilisateur.";
    }

    /**
     * Supprimer un utilisateur
     */
    @Transactional
    public String deleteUser(Long userId) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));

        utilisateurRepository.delete(user);
        return "Utilisateur supprimé avec succès.";
    }

    /**
     * Mettre à jour un utilisateur
     */
    @Transactional
    public Utilisateur updateUser(Long userId, CreateEmployeeRequest request) {
        Utilisateur user = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("Utilisateur introuvable."));

        // Vérifier si le nouvel email est déjà utilisé par un autre utilisateur
        if (!user.getEmail().equals(request.getEmail()) && 
            utilisateurRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Cet email est déjà utilisé.");
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new IllegalStateException("Rôle introuvable."));

        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setTelephone(request.getTelephone());
        user.setPoste(request.getPoste());
        user.setSpecialite(request.getSpecialite());
        user.setService(request.getService());
        user.setRole(role);

        return utilisateurRepository.save(user);
    }

    // =========================
    // UTILITAIRES
    // =========================

    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder pw = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < 10; i++) {
            pw.append(chars.charAt(r.nextInt(chars.length())));
        }
        return pw.toString();
    }
}
