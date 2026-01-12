package com.example.gestion_users.Dao;

import com.example.gestion_users.Entity.Demande;
import com.example.gestion_users.Entity.StatusDemande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemandeRepository extends JpaRepository<Demande, Long> {
    List<Demande> findByDemandeurId(Long id);

    List<Demande> findByStatus(StatusDemande status);

    List<Demande> findByTypeDemande(String typeDemande);
}
