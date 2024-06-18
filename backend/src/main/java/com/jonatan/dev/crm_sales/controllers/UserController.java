package com.jonatan.dev.crm_sales.controllers;

import java.util.List;
import java.util.Optional;
import com.jonatan.dev.crm_sales.domains.page.UsersPage;
import com.jonatan.dev.crm_sales.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import com.jonatan.dev.crm_sales.domains.User;
import lombok.extern.java.Log;

@Log
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public User getUser(@Argument String id) {
        return userService.getUserById(id);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public UsersPage getUsers(
            @Argument Optional<Integer> page,
            @Argument Optional<Integer> size,
            @Argument Optional<String> filter
    ) {
        if (page.isPresent()) {
            if (page.get() <= 1) {
                page = Optional.of(0);
            } else {
                page = Optional.of(page.get() - 1);
            }
        }
        Pageable pageable = PageRequest.of(page.orElse(0), size.orElse(Integer.MAX_VALUE));
        Page<User> userPage = userService.getUsers(pageable, filter);

        List<User> users = userPage.getContent();
        long totalCount = userPage.getTotalElements();
        int totalPages = userPage.getTotalPages();
        return new UsersPage(users, (int) totalCount, totalPages);
    }
}
