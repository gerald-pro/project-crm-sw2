package com.jonatan.dev.crm_sales.controllers;

import com.jonatan.dev.crm_sales.domains.Contact;
import com.jonatan.dev.crm_sales.domains.Lead;
import com.jonatan.dev.crm_sales.domains.User;
import com.jonatan.dev.crm_sales.domains.input.LeadInput;
import com.jonatan.dev.crm_sales.domains.page.LeadsPage;
import com.jonatan.dev.crm_sales.services.ContactService;
import com.jonatan.dev.crm_sales.services.LeadService;
import com.jonatan.dev.crm_sales.services.UserService;
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

@Controller
public class LeadController implements GraphQLMutationResolver {
    @Autowired
    private LeadService leadService;
    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;


    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Lead> getAllLeads() {
        return leadService.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public LeadsPage getLeads(@Argument int page, @Argument int size) {
        Page<Lead> leadPage = leadService.findAllOrderedByCreatedDate(page, size);

        List<Lead> leads = leadPage.getContent();
        long totalCount = leadPage.getTotalElements();
        int totalPages = leadPage.getTotalPages();
        return new LeadsPage(leads, (int) totalCount, totalPages);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Lead getLead(@Argument String id) {
        return  leadService.findById(id).orElseThrow(() -> new NoSuchElementException("No se encontró el lead con id: " + id));

    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Lead createLead(@Argument LeadInput input) {
        Lead lead = new Lead();
        lead.setTitle(input.getTitle());

        User user = userService.findById(input.getUserId()).orElseThrow(() -> new NoSuchElementException("No se encontró el usuario con id: " + input.getUserId()));
        lead.setUserId(user.getId());

        Contact contact = contactService.findById(input.getContactId()).orElseThrow(() -> new NoSuchElementException("No se encontró el contacto con id: " + input.getContactId()));
        lead.setContactId(contact.getId());

        lead.setValue(input.getValue());
        lead.setStatus(input.getStatus());
        return leadService.create(lead);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Lead updateLead(@Argument String id, @Argument LeadInput input) {
        Lead lead = leadService.findById(id).orElseThrow(() -> new NoSuchElementException("Lead con id " + id + " no encontrado."));

        if (input.getTitle() != null) {
            lead.setTitle(input.getTitle());
        }
        if (input.getDescription() != null) {
            lead.setDescription(input.getDescription());
        }

        if (input.getUserId() != null) {
            User user = userService.findById(input.getUserId()).orElseThrow(() -> new NoSuchElementException("No se encontró el usuario con id: " + input.getUserId()));
            lead.setUserId(user.getId());
        }

        if (input.getContactId() != null) {
            Contact contact = contactService.findById(input.getContactId()).orElseThrow(() -> new NoSuchElementException("No se encontró el contacto con id: " + input.getContactId()));
            lead.setContactId(contact.getId());
        }
        if (input.getValue() != null) {
            lead.setValue(input.getValue());
        }
        if (input.getStatus() != null) {
            lead.setStatus(input.getStatus());
        }

        return leadService.update(lead);
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
