package com.jonatan.dev.crm_sales.repositories;

import com.jonatan.dev.crm_sales.domains.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
