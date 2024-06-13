package com.jonatan.dev.crm_sales.services;

import com.jonatan.dev.crm_sales.domains.Lead;
import com.jonatan.dev.crm_sales.domains.input.LeadInput;
import com.jonatan.dev.crm_sales.repositories.LeadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LeadService {
    private final LeadRepository leadRepository;

    @Autowired
    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public Page<Lead> findAll(Pageable pageable) {
        return leadRepository.findAll(pageable);
    }

    public Optional<Lead> findById(String id) {
        return leadRepository.findById(id);
    }

    public Lead create(Lead lead) {
        return leadRepository.save(lead);
    }

    public Lead update(String leadId, LeadInput updatedLead) {
        Lead lead = findById(leadId).orElseThrow(() -> new NoSuchElementException("Lead con id " + leadId + " no encontrado."));
        lead.setTitle(updatedLead.getTitle());
        lead.setDescription(updatedLead.getDescription());
        lead.setUserId(updatedLead.getUserId());
        lead.setContactId(updatedLead.getContactId());
        lead.setValue(updatedLead.getValue());
        lead.setStatus(updatedLead.getStatus());
        return leadRepository.save(lead);
    }

    public void delete(String id) {
        leadRepository.deleteById(id);
    }
}
