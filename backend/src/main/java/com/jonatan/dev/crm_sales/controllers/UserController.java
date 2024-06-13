package com.jonatan.dev.crm_sales.controllers;

import java.util.List;

import com.jonatan.dev.crm_sales.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
