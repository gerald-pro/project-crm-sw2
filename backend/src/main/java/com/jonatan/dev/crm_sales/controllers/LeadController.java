package com.jonatan.dev.crm_sales.controllers;

import com.jonatan.dev.crm_sales.domains.Lead;
import com.jonatan.dev.crm_sales.domains.input.LeadInput;
import com.jonatan.dev.crm_sales.domains.page.LeadsPage;
import com.jonatan.dev.crm_sales.services.LeadService;
import graphql.kickstart.tools.GraphQLMutationResolver;
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
public class LeadController implements GraphQLMutationResolver {
    private final LeadService leadService;

    @Autowired
    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Lead> getAllLeads() {
        return leadService.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public LeadsPage getLeads(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Lead> leadPage = leadService.findAll(pageable);

        List<Lead> leads = leadPage.getContent();
        long totalCount = leadPage.getTotalElements();
        int totalPages = leadPage.getTotalPages();
        return new LeadsPage(leads, (int) totalCount, totalPages);
    }


    public Optional<Lead> getLeadById(String id) {
        return leadService.findById(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Lead createLead(@Argument LeadInput input) {
        Lead lead = new Lead();
        lead.setTitle(input.getTitle());
        lead.setUserId(input.getUserId());
        lead.setContactId(input.getContactId());
        lead.setValue(input.getValue());
        lead.setStatus(input.getStatus());
        return leadService.create(lead);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Lead updateLead(@Argument String id, @Argument LeadInput input) {
        return leadService.update(id, input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteLead(@Argument String id) {
        if (leadService.findById(id).isEmpty()) {
            throw new NoSuchElementException("No se encontró el lead con id: " + id);
        } else {
            leadService.delete(id);
        }
    }
}
