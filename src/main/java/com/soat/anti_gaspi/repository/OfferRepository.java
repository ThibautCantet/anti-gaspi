package com.soat.anti_gaspi.repository;

import java.util.UUID;
import com.soat.anti_gaspi.model.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends CrudRepository<Offer, UUID> {
}
