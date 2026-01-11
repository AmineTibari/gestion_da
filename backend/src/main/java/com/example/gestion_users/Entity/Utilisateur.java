package com.example.gestion_users.Entity;


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Utilisateur {

    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String cni;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getCni() {
        return cni;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
