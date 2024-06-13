package com.jonatan.dev.crm_sales.domains;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String name;
    @Indexed(unique = true)
    private String username;
    private String email;
    private String password;
    private List<Role> roles;
    @CreatedDate
    private LocalDateTime createdAt;
}