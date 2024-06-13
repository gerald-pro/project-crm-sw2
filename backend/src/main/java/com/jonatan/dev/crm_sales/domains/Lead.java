package com.jonatan.dev.crm_sales.domains;

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
@Document(collection = "leads")
public class Lead {
    @Id
    private String id;
    private String title;
    private String description;
    private String userId;
    private String contactId;
    private Float value;
    private String status = "nuevo";
    @CreatedDate
    private LocalDateTime createdAt;
}
