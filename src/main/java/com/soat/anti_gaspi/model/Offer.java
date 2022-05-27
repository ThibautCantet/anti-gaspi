package com.soat.anti_gaspi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Offer {
    private final String company;
    private final String title;
    private final String description;
    private final String email;
    private final String address;
    private final String disponibilite;
    private final String expiration;
    @Id
    @GeneratedValue
    private Integer id;

    public Offer(String company, String title, String description, String email, String address, String disponibilite, String expiration) {

        this.company = company;
        this.title = title;
        this.description = description;
        this.email = email;
        this.address = address;
        this.disponibilite = disponibilite;
        this.expiration = expiration;
    }

    public Integer getId() {
        return id;
    }
}
