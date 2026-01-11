package com.example.gestion_users.Service.Impl;

import com.example.gestion_users.Dao.AdministrateurRepository;
import com.example.gestion_users.Entity.Administrateur;
import com.example.gestion_users.Service.facade.AdministrateurService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministrateurServiceImpl implements AdministrateurService {
    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Override
    public List<Administrateur> consulterAdministrateurs() {
        return administrateurRepository.findAll();
    }

    @Override
    public Administrateur consulterAdministrateur(Long id) {
        return administrateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrateur introuvable"));
    }

    @Override
    public void ajouterAdministrateur(Administrateur administrateur) {
         administrateurRepository.save(administrateur);
    }

    @Override
    @Transactional
    public void supprimerAdministrateur(Long id) {
        administrateurRepository.deleteById(id);
    }

    @Override
    public void updateAdministrateurPassword(Long id, String newPassword) {
        Administrateur administrateur = consulterAdministrateur(id);
        administrateur.setPassword(newPassword);
        administrateurRepository.save(administrateur);
    }
}
