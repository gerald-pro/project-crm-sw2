package com.jonatan.dev.crm_sales.controllers;

import com.jonatan.dev.crm_sales.domains.SaleDetail;
import com.jonatan.dev.crm_sales.domains.page.SaleDetailsPage;
import com.jonatan.dev.crm_sales.services.SaleDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;

@Controller
public class SaleDetailController {
    private final SaleDetailService saleDetailService;

    @Autowired
    public SaleDetailController(SaleDetailService saleDetailService) {
        this.saleDetailService = saleDetailService;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public SaleDetailsPage getSalesDetails(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<SaleDetail> saleDetailPage = saleDetailService.findAll(pageable);

        List<SaleDetail> saleDetails = saleDetailPage.getContent();
        long totalCount = saleDetailPage.getTotalElements();
        int totalPages = saleDetailPage.getTotalPages();
        return new SaleDetailsPage(saleDetails, (int) totalCount, totalPages);
    }

    public SaleDetail getSaleDetailById(String id) {
        Optional<SaleDetail> saleDetail = saleDetailService.findById(id);
        return saleDetail.orElse(null);
    }

    public SaleDetail createSaleDetail(String description) {
        SaleDetail saleDetail = new SaleDetail();
        saleDetail.setDescription(description);
        return saleDetailService.save(saleDetail);
    }
}
