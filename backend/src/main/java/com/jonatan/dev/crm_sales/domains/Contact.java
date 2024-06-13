package com.jonatan.dev.crm_sales.domains;

import com.jonatan.dev.crm_sales.domains.input.ContactInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "contacts")
public class Contact {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    @CreatedDate
    private LocalDateTime createdAt;

    public Contact(ContactInput input){
        this.setName(input.getName());
        this.setEmail(input.getEmail());
        this.setPhone(input.getPhone());
        this.setAddress(input.getAddress());
        this.setCreatedAt(input.getCreatedAt());
    }
}
