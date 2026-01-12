package com.example.gestion_users;

import com.example.gestion_users.Dao.AdministrateurRepository;
import com.example.gestion_users.Dao.UtilisateurRepository;
import com.example.gestion_users.Entity.Administrateur;
import com.example.gestion_users.Entity.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
    }

    private void createDefaultAdmin() {
        String email = "admin@gmail.com";
        Optional<Utilisateur> existingUser = utilisateurRepository.findByEmail(email);

        if (existingUser.isEmpty()) {
            Administrateur admin = new Administrateur();
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setEmail(email);
            admin.setPassword("admin123");
            admin.setCni("ADMIN001");
            admin.setType("ADMINISTRATEUR");
            admin.setRole("SUPER_ADMIN");
            admin.setTitreFonction("Manager");

            administrateurRepository.save(admin);
            System.out.println("Default Admin created: " + email + " / admin123");
        } else {
            System.out.println("Admin check: User " + email + " already exists.");
        }
    }
}
