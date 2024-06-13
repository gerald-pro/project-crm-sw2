package com.jonatan.dev.crm_sales.controllers;

import com.jonatan.dev.crm_sales.authentication.JwtUtils;
import com.jonatan.dev.crm_sales.domains.AuthResponse;
import com.jonatan.dev.crm_sales.domains.User;
import com.jonatan.dev.crm_sales.domains.input.UserInput;
import com.jonatan.dev.crm_sales.services.AuthService;
import com.jonatan.dev.crm_sales.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @MutationMapping
    public AuthResponse login(@Argument String username, @Argument String password) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("El usuario no está registrado");
        }

        String token = authService.login(username, password);
        return new AuthResponse(user, token);
    }

    @MutationMapping
    public AuthResponse register(@Argument String name, @Argument String username, @Argument String password, @Argument String email) {
        if (userService.findByUsername(username) != null) {
            throw new IllegalArgumentException("El usuario ya está registrado");
        }

        UserInput input = new UserInput(name, username, password, email);
        User user = userService.register(input);
        String token = JwtUtils.generateToken(user.getUsername());
        return new AuthResponse(user, token);
    }
}
