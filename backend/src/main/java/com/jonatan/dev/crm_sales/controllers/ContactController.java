package com.jonatan.dev.crm_sales.controllers;

import com.jonatan.dev.crm_sales.domains.Contact;
import com.jonatan.dev.crm_sales.domains.input.ContactInput;
import com.jonatan.dev.crm_sales.domains.page.ContactsPage;
import com.jonatan.dev.crm_sales.services.ContactService;
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
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @QueryMapping
    public ContactsPage getContacts(
            @Argument int page,
            @Argument int size,
            @Argument Optional<String> filter) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Contact> contactPage = contactService.getContacts(pageable, filter);

        List<Contact> contacts = contactPage.getContent();
        long totalCount = contactPage.getTotalElements();
        int totalPages = contactPage.getTotalPages();
        return new ContactsPage(contacts, (int) totalCount, totalPages);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Contact getContactById(@Argument String id) {
        return contactService.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Contact not found with id: " + id));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Contact createContact(@Argument ContactInput input) {
        return contactService.create(input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Contact updateContact(@Argument String id, @Argument ContactInput input) {
        return contactService.update(id, input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteContact(@Argument String id) {
        if (contactService.findById(id).isEmpty()) {
            throw new NoSuchElementException("No se encontró el contacto con id: " + id);
        }
        contactService.delete(id);
    }
}
