package com.soat.anti_gaspi.repository;

import com.soat.anti_gaspi.model.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactRepository extends CrudRepository<Contact, UUID> {
}
