package com.backoffice.atelier.Controller;

import com.backoffice.atelier.entities.Permission;
import com.backoffice.atelier.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionRepository permissionRepository;

    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Permission getPermissionById(@PathVariable Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission non trouvée"));
    }

    @GetMapping("/actives")
    public List<Permission> getPermissionsActives() {
        return permissionRepository.findByActifTrue();
    }

    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionRepository.save(permission);
    }

    @PutMapping("/{id}")
    public Permission updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission non trouvée"));
        
        existing.setCode(permission.getCode());
        existing.setDescription(permission.getDescription());
        existing.setActif(permission.isActif());
        
        return permissionRepository.save(existing);
    }

    @DeleteMapping("/{id}")
    public void deletePermission(@PathVariable Long id) {
        permissionRepository.deleteById(id);
    }
}