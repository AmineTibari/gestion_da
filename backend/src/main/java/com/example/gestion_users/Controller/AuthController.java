package com.example.gestion_users.Controller;

import com.example.gestion_users.Dto.LoginRequest;
import com.example.gestion_users.Entity.Utilisateur;
import com.example.gestion_users.Service.facade.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/login")
    public Utilisateur login(@RequestBody LoginRequest request) {
        return utilisateurService.seConnecter(
                request.getEmail(),
                request.getPassword());
    }

    @PostMapping("/register")
    public Utilisateur register(@RequestBody Utilisateur utilisateur, @RequestParam(required = false) String type) {
        return utilisateurService.creerCompte(utilisateur, type);
    }
}
