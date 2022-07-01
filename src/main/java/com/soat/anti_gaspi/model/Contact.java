package com.soat.anti_gaspi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Contact {
    @Id
    private UUID id;
    @Column
    private String lastName;
    @Column
    private String firstName;
    @Column
    private String phoneNumber;
    @Column
    private String messageContent;
    @Column
    private String offerId;

    public Contact(String lastName, String firstName, String phoneNumber, String messageContent, String offerId) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.messageContent = messageContent;
        this.offerId = offerId;
    }

    public Contact() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getOfferId() {
        return offerId;
    }
}
