package com.jonatan.dev.crm_sales.domains;

import com.jonatan.dev.crm_sales.domains.input.ActivityInput;
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
    private String id;

    private String title;

    private String description;

    private ActivityType type;

    private String location;

    private String activityDate;

    private String leadId;


    public Activity(ActivityInput input) {
        title = input.getTitle();
        description = input.getDescription();
        type = input.getType();
        location = input.getLocation();
        activityDate = input.getActivityDate();
        leadId = input.getLeadId();
    }
}

