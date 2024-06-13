package com.jonatan.dev.crm_sales.controllers;

import com.jonatan.dev.crm_sales.domains.Product;
import com.jonatan.dev.crm_sales.domains.page.ProductsPage;
import com.jonatan.dev.crm_sales.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ProductsPage getProducts(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> productPage = productService.findAll(pageable);

        List<Product> products = productPage.getContent();
        long totalCount = productPage.getTotalElements();
        int totalPages = productPage.getTotalPages();
        return new ProductsPage(products, (int) totalCount, totalPages);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Product getProductById(@Argument String id) {
        Optional<Product> product = productService.findById(id);
        return product.orElse(null);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Product createProduct(@Argument String description) {
        Product product = new Product();
        product.setDescription(description);
        return productService.save(product);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Product updateProduct(@Argument String id, @Argument String description) {
        return productService.save(new Product(id, description));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteProduct(@Argument String id) {
        if (productService.findById(id).isEmpty()) {
            throw new NoSuchElementException("No se encontró el producto con id: " + id);
        } else {
            productService.delete(id);
        }
    }
}
