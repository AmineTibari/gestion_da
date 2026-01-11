package com.example.gestion_users.Dto;

public class DemandeDto {
    private String titreDemande;
    private String typeDemande;
    private Long idDemandeur;

    public String getTitreDemande() {
        return titreDemande;
    }

    public void setTitreDemande(String titreDemande) {
        this.titreDemande = titreDemande;
    }

    public String getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(String typeDemande) {
        this.typeDemande = typeDemande;
    }

    public Long getIdDemandeur() {
        return idDemandeur;
    }

    public void setIdDemandeur(Long idDemandeur) {
        this.idDemandeur = idDemandeur;
    }
}
