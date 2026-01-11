package com.example.gestion_users.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
public class Demandeur extends Utilisateur {

    private Integer idenDemandeur;
    private String status;

}
