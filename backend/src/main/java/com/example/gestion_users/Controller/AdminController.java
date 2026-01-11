package com.example.gestion_users.Controller;

import com.example.gestion_users.Entity.Administrateur;
import com.example.gestion_users.Service.facade.AdministrateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/admin/administrateurs")
public class AdminController {

    @Autowired
    private AdministrateurService administrateurService;

    @GetMapping("/")
    public List<Administrateur> getAll() {
        return administrateurService.consulterAdministrateurs();
    }

    @GetMapping("/{id}")
    public Administrateur getOne(@PathVariable Long id) {
        return administrateurService.consulterAdministrateur(id);
    }

    @PostMapping
    public void create(@RequestBody Administrateur admin) {
         administrateurService.ajouterAdministrateur(admin);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        administrateurService.supprimerAdministrateur(id);
    }

    @PutMapping("/{id}/password")
    public void updatePassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        administrateurService.updateAdministrateurPassword(id, newPassword);
    }

}
