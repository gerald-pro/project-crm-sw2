package com.jonatan.dev.crm_sales.services;

import com.jonatan.dev.crm_sales.domains.SaleDetail;
import com.jonatan.dev.crm_sales.repositories.SaleDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SaleDetailService {
    private final SaleDetailRepository saleDetailRepository;

    @Autowired
    public SaleDetailService(SaleDetailRepository saleDetailRepository) {
        this.saleDetailRepository = saleDetailRepository;
    }

    public List<SaleDetail> all() {
        return saleDetailRepository.findAll();
    }

    public Page<SaleDetail> findAll(Pageable pageable) {
        return saleDetailRepository.findAll(pageable);
    }

    public Optional<SaleDetail> findById(String id) {
        return saleDetailRepository.findById(id);
    }

    public SaleDetail save(SaleDetail data) {
        return saleDetailRepository.save(data);
    }

    public void delete(String id) {
        saleDetailRepository.deleteById(id);
    }
}
