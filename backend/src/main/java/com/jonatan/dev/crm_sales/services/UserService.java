package com.jonatan.dev.crm_sales.services;

import com.jonatan.dev.crm_sales.domains.Role;
import com.jonatan.dev.crm_sales.domains.input.UserInput;
import com.jonatan.dev.crm_sales.exceptions.UserServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jonatan.dev.crm_sales.domains.User;
import com.jonatan.dev.crm_sales.repositories.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsServiceInterface {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserInput userInput) {
        User user = new User();
        user.setName(userInput.getName());
        user.setUsername(userInput.getUsername());
        user.setEmail(userInput.getEmail());
        user.setPassword(passwordEncoder.encode(userInput.getPassword()));
        user.setRoles(Arrays.asList(Role.ROLE_USER));
        return userRepository.save(user);
    }

    // Método para obtener un usuario por username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserServiceException("Usuario no encontrado. id: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}