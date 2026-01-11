package com.example.gestion_users.Controller;


import com.example.gestion_users.Entity.Demande;
import com.example.gestion_users.Entity.StatusDemande;
import com.example.gestion_users.Service.facade.DemandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.gestion_users.Dto.DemandeDto;

import java.util.List;

@RestController
@RequestMapping("/api/demandes")
@CrossOrigin("*")
public class DemandeController {

    @Autowired
    private DemandeService demandeService;

    // Soumettre une demande
    @PostMapping("/soumettre")
    public Demande soumettre(@ModelAttribute DemandeDto dto) {
        return demandeService.soumettreDemande(dto);
    }

    // Assigner demande
    @PutMapping("/assign/{idDemande}/{idAdmin}")
    public Demande assigner(
            @PathVariable Long idDemande,
            @PathVariable Long idAdmin) {
        return demandeService.assignerDemande(idDemande, idAdmin);
    }

    // Valider une demande
    @PutMapping("/valider/{id}")
    public Demande valider(@PathVariable Long id) {
        return demandeService.validerDemande(id);
    }

    // Rejeter une demande
    @PutMapping("/rejeter/{id}")
    public Demande rejeter(@PathVariable Long id) {
        return demandeService.rejeterDemande(id);
    }

    // Traiter une demande
    @PutMapping("/traiter/{id}")
    public Demande traiter(@PathVariable Long id) {
        return demandeService.traiterDemande(id);
    }

    // Consulter toutes les demandes
    @GetMapping("/")
    public List<Demande> findAll() {
        return demandeService.consulterLesDemandes();
    }

    // Consulter une seule demande
    @GetMapping("/{id}")
    public Demande getById(@PathVariable Long id) {
        return demandeService.consulterDemande(id);
    }

    // Filtrer par status
    @GetMapping("/filtrer")
    public List<Demande> filtrer(@RequestParam StatusDemande status) {
        return demandeService.filtrerDemandes(status);
    }
}
