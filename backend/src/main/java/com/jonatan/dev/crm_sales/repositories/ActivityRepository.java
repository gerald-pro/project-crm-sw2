package com.jonatan.dev.crm_sales.repositories;

import com.jonatan.dev.crm_sales.domains.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository extends MongoRepository<Activity, String> {
    List<Activity> findByLeadId(String leadId);
    void deleteByLeadId(String leadId);
}
