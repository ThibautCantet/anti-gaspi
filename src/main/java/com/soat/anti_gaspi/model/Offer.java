package com.soat.anti_gaspi.model;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    private LocalDate availabilityDate;
    @Column
    private LocalDate expirationDate;

    @Column
    private Status status;

    public Offer(String company, String title, String description, String email, String address, LocalDate availabilityDate, LocalDate expirationDate) {
        this.company = company;
        this.title = title;
        this.description = description;
        this.email = email;
        this.address = address;
        this.availabilityDate = availabilityDate;
        this.expirationDate = expirationDate;
        this.status = Status.UNPUBLISHED;
    }

    public Offer(UUID id, String company, String title, String description, String email, String address, LocalDate availabilityDate, LocalDate expirationDate, Status status) {
        this.id = id;
        this.company = company;
        this.title = title;
        this.description = description;
        this.email = email;
        this.address = address;
        this.availabilityDate = availabilityDate;
        this.expirationDate = expirationDate;
        this.status = status;
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

    public LocalDate getAvailabilityDate() {
        return availabilityDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }
}
