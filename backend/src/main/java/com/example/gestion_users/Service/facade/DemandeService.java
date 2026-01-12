package com.example.gestion_users.Service.facade;

import com.example.gestion_users.Dto.DemandeDto;
import com.example.gestion_users.Entity.Demande;
import com.example.gestion_users.Entity.StatusDemande;

import java.util.List;

public interface DemandeService {

    Demande soumettreDemande(DemandeDto dto);

    Demande validerDemande(Long id);

    Demande rejeterDemande(Long id);

    Demande traiterDemande(Long id);

    List<Demande> consulterLesDemandes();

    Demande consulterDemande(Long id);

    List<Demande> filtrerDemandes(StatusDemande status);

    Demande getDemande(Long id);

    Demande assignerDemande(Long idDemande, Long idAdmin);

    List<Demande> consulterMesDemandes(Long idDemandeur);

}
