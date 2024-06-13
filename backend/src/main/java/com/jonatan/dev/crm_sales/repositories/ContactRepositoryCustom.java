package com.jonatan.dev.crm_sales.repositories;

import com.jonatan.dev.crm_sales.domains.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ContactRepositoryCustom {
    Page<Contact> findAll(Pageable pageable, Optional<String> filter);
}
