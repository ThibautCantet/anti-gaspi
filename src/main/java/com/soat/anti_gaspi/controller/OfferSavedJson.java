package com.soat.anti_gaspi.controller;

import java.time.LocalDate;
import java.util.UUID;

public record OfferSavedJson(UUID id, String companyName, String title, String description, String email, String address, LocalDate availabilityDate, LocalDate expirationDate) {

}
