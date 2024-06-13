package com.jonatan.dev.crm_sales.domains.page;

import com.jonatan.dev.crm_sales.domains.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsPage {
    private List<Product> products;
    private int totalCount;
    private int totalPages;
}
