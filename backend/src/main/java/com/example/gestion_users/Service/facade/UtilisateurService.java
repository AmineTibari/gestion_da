package com.example.gestion_users.Service.facade;
import com.example.gestion_users.Entity.Utilisateur;

import java.util.Optional;

public interface UtilisateurService {
    Utilisateur seConnecter(String email, String password);
    Utilisateur creerCompte(Utilisateur utilisateur, String type);
}