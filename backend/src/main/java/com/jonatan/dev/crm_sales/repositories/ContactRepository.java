package com.jonatan.dev.crm_sales.repositories;

import com.jonatan.dev.crm_sales.domains.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String>, ContactRepositoryCustom  {
}
