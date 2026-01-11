package com.example.gestion_users.Service.facade;

import com.example.gestion_users.Entity.Administrateur;

import java.util.List;

public interface AdministrateurService {

    List<Administrateur> consulterAdministrateurs();
    Administrateur consulterAdministrateur(Long Id);
    void ajouterAdministrateur(Administrateur admin);
    void supprimerAdministrateur(Long id);
    void updateAdministrateurPassword(Long id, String newPassword);

}
