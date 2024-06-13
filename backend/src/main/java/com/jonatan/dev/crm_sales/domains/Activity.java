package com.jonatan.dev.crm_sales.domains;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "activities")
public class Activity {
    @Id
    private int id;

    private String title;

    private String description;

    private String location;

    private LocalDateTime activityDate;

    private int leadId;
}
