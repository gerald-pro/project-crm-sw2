package com.jonatan.dev.crm_sales.domains.page;

import com.jonatan.dev.crm_sales.domains.Contact;
import com.jonatan.dev.crm_sales.domains.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersPage {
    private List<User> users;
    private int totalCount;
    private int totalPages;
}
