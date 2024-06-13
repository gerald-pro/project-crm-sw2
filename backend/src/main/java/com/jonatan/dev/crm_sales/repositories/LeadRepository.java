package com.jonatan.dev.crm_sales.repositories;

import com.jonatan.dev.crm_sales.domains.Lead;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeadRepository extends MongoRepository<Lead, String> {
}
