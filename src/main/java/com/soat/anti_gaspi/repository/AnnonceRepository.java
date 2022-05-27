package com.soat.anti_gaspi.repository;

import com.soat.anti_gaspi.model.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnonceRepository extends CrudRepository<Offer, Integer> {
}
