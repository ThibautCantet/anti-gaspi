package com.soat.anti_gaspi.model;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Offer {
    @Id
    private UUID id;
    @Column
    private String company;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String email;
    @Column
    private String address;
    @Column
    private LocalDate disponibilite;
    @Column
    private LocalDate expiration;

    public Offer(String company, String title, String description, String email, String address, LocalDate disponibilite, LocalDate expiration) {
        this.company = company;
        this.title = title;
        this.description = description;
        this.email = email;
        this.address = address;
        this.disponibilite = disponibilite;
        this.expiration = expiration;
    }

    public Offer() {

    }

    public UUID getId() {
        return id;
    }


    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDisponibilite() {
        return disponibilite;
    }

    public LocalDate getExpiration() {
        return expiration;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
