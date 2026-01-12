package com.example.gestion_users.Service.Impl;

import com.example.gestion_users.Dao.AdministrateurRepository;
import com.example.gestion_users.Dao.DemandeurRepository;
import com.example.gestion_users.Dto.DemandeDto;
import com.example.gestion_users.Entity.Demandeur;
import com.example.gestion_users.Entity.StatusDemande;
import com.example.gestion_users.Service.facade.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.gestion_users.Entity.Demande;
import com.example.gestion_users.Entity.Administrateur;
import com.example.gestion_users.Dao.DemandeRepository;

import java.io.IOException;
import java.time.LocalDateTime;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class DemandeServiceImpl implements DemandeService {

    @Autowired
    private DemandeRepository demandeRepository;

    @Autowired
    private DemandeurRepository demandeurRepository;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    // soumettre une demande
    @Override
    public Demande soumettreDemande(DemandeDto dto) {

        Demandeur demandeur = demandeurRepository.findById(dto.getIdDemandeur())
                .orElseThrow(() -> new RuntimeException("Demandeur introuvable"));

        Demande demande = new Demande();
        demande.setTitreDemande(dto.getTitreDemande());
        demande.setTypeDemande(dto.getTypeDemande());
        demande.setDemandeur(demandeur);
        int randomNumber = (int) (Math.random() * 90000) + 10000;
        demande.setReference("REF-" + randomNumber);
        demande.setDateDepot(LocalDateTime.now());
        demande.setStatus(StatusDemande.SOUMISE);
        demande.setDescription(dto.getDescription());

        if (dto.getFichier() != null && !dto.getFichier().isEmpty()) {
            try {
                Path uploadPath = Paths.get("uploads");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath); // créer dossier si besoin
                }

                String fileName = System.currentTimeMillis() + "_" + dto.getFichier().getOriginalFilename();
                Path destination = uploadPath.resolve(fileName);

                Files.copy(dto.getFichier().getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

                demande.setNomFichier(fileName);

            } catch (IOException e) {
                // transformer IOException en RuntimeException
                throw new RuntimeException("Erreur lors de l'upload du fichier", e);
            }
        }

        return demandeRepository.save(demande);
    }

    // valider une demande
    @Override
    public Demande validerDemande(Long id) {
        Demande demande = getDemande(id);
        demande.setStatus(StatusDemande.VALIDEE);
        return demandeRepository.save(demande);
    }

    // rejeter une demande
    @Override
    public Demande rejeterDemande(Long id) {
        Demande demande = getDemande(id);
        demande.setStatus(StatusDemande.REJETEE);
        return demandeRepository.save(demande);
    }

    // traiter une demande
    @Override
    public Demande traiterDemande(Long id) {
        Demande demande = getDemande(id);
        demande.setStatus(StatusDemande.EN_TRAITEMENT);
        return demandeRepository.save(demande);
    }

    // consulter toutes les demandes
    @Override
    public List<Demande> consulterLesDemandes() {
        return demandeRepository.findAll();
    }

    // consulter une seule demande
    @Override
    public Demande consulterDemande(Long id) {
        return getDemande(id);
    }

    // filtrer demandes
    @Override
    public List<Demande> filtrerDemandes(StatusDemande status) {
        return demandeRepository.findByStatus(status);
    }

    @Override
    public Demande getDemande(Long id) {
        return demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
    }

    @Override
    public Demande assignerDemande(Long idDemande, Long idAdmin) {
        Demande demande = demandeRepository.findById(idDemande)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        Administrateur admin = administrateurRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Admin introuvable"));

        demande.setAdministrateur(admin);
        demande.setStatus(StatusDemande.EN_TRAITEMENT);

        return demandeRepository.save(demande);
    }

    @Override
    public List<Demande> consulterMesDemandes(Long idDemandeur) {
        return demandeRepository.findByDemandeurId(idDemandeur);
    }

}
