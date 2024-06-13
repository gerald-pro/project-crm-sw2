package com.jonatan.dev.crm_sales.domains.page;

import com.jonatan.dev.crm_sales.domains.Contact;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsPage {
    private List<Contact> contacts;
    private int totalCount;
    private int totalPages;
}
