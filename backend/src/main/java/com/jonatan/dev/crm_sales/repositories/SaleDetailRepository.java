package com.jonatan.dev.crm_sales.repositories;

import com.jonatan.dev.crm_sales.domains.SaleDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaleDetailRepository extends MongoRepository<SaleDetail, String> {
}
