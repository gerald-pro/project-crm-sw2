package com.jonatan.dev.crm_sales.services;

import com.jonatan.dev.crm_sales.domains.Product;
import com.jonatan.dev.crm_sales.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> all() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        Product contact = findById(product.getId()).orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));
        return productRepository.save(contact);
    }

    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
