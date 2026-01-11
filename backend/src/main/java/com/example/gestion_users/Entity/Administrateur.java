package com.example.gestion_users.Entity;

import jakarta.persistence.*;


@Entity
@Table(name = "administrateur")
@PrimaryKeyJoinColumn(name = "id")
public class Administrateur extends Utilisateur {

    @Column(name = "titre_fonction")
    private String titreFonction;

    @Column(name = "role")
    private String role;

    @Column(name = "idenAdministrateur")
    private Integer idenAdministrateur;

    public String getTitreFonction() { return titreFonction; }
    public void setTitreFonction(String titreFonction) { this.titreFonction = titreFonction; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
