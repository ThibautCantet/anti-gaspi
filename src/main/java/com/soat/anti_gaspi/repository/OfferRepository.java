package com.soat.anti_gaspi.repository;

import java.util.List;
import java.util.UUID;

import com.soat.anti_gaspi.model.Offer;
import com.soat.anti_gaspi.model.Status;
import com.soat.anti_gaspi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends PagingAndSortingRepository<Offer, UUID> {

    Page<Offer> findAll(Pageable pageable);
    Page<Offer> findAllByStatus(Status status, Pageable pageable);

    List<Offer> findOffersByUser(User user);
}
