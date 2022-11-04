package com.soat.anti_gaspi.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OfferPage(List<SavedOffer> content, Long total) {
}
