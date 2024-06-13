package com.jonatan.dev.crm_sales.domains.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInput {
    String name;
    String username;
    String password;
    String email;
}
