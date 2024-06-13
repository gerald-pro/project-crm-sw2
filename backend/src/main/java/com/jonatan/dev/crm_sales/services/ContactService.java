package com.jonatan.dev.crm_sales.services;

import com.jonatan.dev.crm_sales.domains.Contact;
import com.jonatan.dev.crm_sales.domains.input.ContactInput;
import com.jonatan.dev.crm_sales.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Page<Contact> getContacts(Pageable pageable, Optional<String> filter) {
        return contactRepository.findAll(pageable, filter);
    }

    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    public Optional<Contact> findById(String id) {
        return contactRepository.findById(id);
    }

    public Contact create(ContactInput input) {
        Contact newContact = new Contact(input);
        return contactRepository.save(newContact);
    }

    public Contact update(String id, ContactInput input) {
        Contact contact = findById(id).orElseThrow(() -> new NoSuchElementException("Contacto no encontrado"));
        contact.setName(input.getName());
        contact.setEmail(input.getEmail());
        contact.setPhone(input.getPhone());
        contact.setAddress(input.getAddress());
        return contactRepository.save(contact);
    }

    public void delete(String id) {
        contactRepository.deleteById(id);
    }
}
