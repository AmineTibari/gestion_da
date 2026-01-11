package com.example.gestion_users.Dao;

import com.example.gestion_users.Entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface AdministrateurRepository extends JpaRepository<Administrateur, Integer> {
    Optional<Administrateur> findByEmail(String email);
    List<Administrateur> findAll();
    Optional<Administrateur> findById(Long id);
    Administrateur save(Administrateur administrateur);
    void deleteById(Long id);
}
