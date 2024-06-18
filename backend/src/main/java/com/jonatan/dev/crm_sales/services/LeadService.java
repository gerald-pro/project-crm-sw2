package com.jonatan.dev.crm_sales.services;

import com.jonatan.dev.crm_sales.domains.Lead;
import com.jonatan.dev.crm_sales.repositories.ActivityRepository;
import com.jonatan.dev.crm_sales.repositories.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LeadService {
    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public Page<Lead> findAll(Pageable pageable) {
        return leadRepository.findAll(pageable);
    }

    public Page<Lead> findAllOrderedByCreatedDate(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return leadRepository.findAll(pageable);
    }

    public Optional<Lead> findById(String id) {
        return leadRepository.findById(id);
    }

    public Lead create(Lead lead) {
        return leadRepository.save(lead);
    }

    public Lead update(Lead updatedLead) {
        return leadRepository.save(updatedLead);
    }

    public void delete(String id) {
        activityRepository.deleteByLeadId(id);
        leadRepository.deleteById(id);
    }
}
