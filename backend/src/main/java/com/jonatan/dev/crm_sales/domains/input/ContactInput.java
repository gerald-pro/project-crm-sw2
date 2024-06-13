package com.jonatan.dev.crm_sales.domains.input;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactInput {
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;
}
