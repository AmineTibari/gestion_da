package com.example.gestion_users.Service.Impl;

import com.example.gestion_users.Dao.UtilisateurRepository;
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

    @Override
    public Utilisateur seConnecter(String email, String password) {
        return utilisateurRepository
                .findByEmailAndPassword(email, password)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Email ou mot de passe incorrect"
                        )
                );
    }

    @Override
    public Utilisateur creerCompte(Utilisateur utilisateur, String type) {

        boolean existe = utilisateurRepository
                .findByEmail(utilisateur.getEmail())
                .isPresent();

        if (existe) {
            throw new RuntimeException("Email déjà utilisé");
        }

        if (type == null || type.isBlank()) {
            utilisateur.setType("Demandeur");
        } else {
            utilisateur.setType(type);
        }

        return utilisateurRepository.save(utilisateur);

    }

}
