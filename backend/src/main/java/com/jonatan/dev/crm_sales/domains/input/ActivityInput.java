package com.jonatan.dev.crm_sales.domains.input;

import com.jonatan.dev.crm_sales.domains.ActivityType;
import lombok.Data;

@Data
public class ActivityInput {
    private String title;
    private String description;
    private ActivityType type;
    private String location;
    private String activityDate;
    private String leadId;
}
