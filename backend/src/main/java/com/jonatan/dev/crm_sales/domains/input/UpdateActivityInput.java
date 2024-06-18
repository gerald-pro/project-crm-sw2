package com.jonatan.dev.crm_sales.domains.input;

import com.jonatan.dev.crm_sales.domains.ActivityType;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class UpdateActivityInput {
    private String id;
    private String title;
    private String description;
    private ActivityType type;
    private String location;
    private String activityDate;
    private String leadId;
}
