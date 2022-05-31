package com.soat.anti_gaspi.controller;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

public record OfferJson(String company, String title, String description, String email, String address, String availabilityDate, String expirationDate) {

}
