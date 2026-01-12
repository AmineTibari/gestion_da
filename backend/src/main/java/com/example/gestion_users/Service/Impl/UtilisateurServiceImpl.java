package com.example.gestion_users.Service.Impl;

import com.example.gestion_users.Dao.DemandeurRepository;
import com.example.gestion_users.Dao.UtilisateurRepository;
import com.example.gestion_users.Entity.Demandeur;
import com.example.gestion_users.Service.facade.UtilisateurService;
import com.example.gestion_users.Entity.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private DemandeurRepository demandeurRepository;

    @Override
    public Utilisateur seConnecter(String email, String password) {
        return utilisateurRepository
                .findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Email ou mot de passe incorrect"));
    }

    @Override
    public Utilisateur creerCompte(Utilisateur utilisateur, String type) {

        boolean existe = utilisateurRepository
                .findByEmail(utilisateur.getEmail())
                .isPresent();

        if (existe) {
            throw new RuntimeException("Email déjà utilisé");
        }

        if (type == null || type.isBlank() || type.equals("Demandeur")) {
            // Create a Demandeur entity
            Demandeur demandeur = new Demandeur();
            demandeur.setEmail(utilisateur.getEmail());
            demandeur.setPassword(utilisateur.getPassword());
            demandeur.setNom(utilisateur.getNom());
            demandeur.setPrenom(utilisateur.getPrenom());
            demandeur.setCni(utilisateur.getCni());
            demandeur.setType("Demandeur");
            return demandeurRepository.save(demandeur);
        } else {
            utilisateur.setType(type);
            return utilisateurRepository.save(utilisateur);
        }

    }

}
