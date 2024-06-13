package com.jonatan.dev.crm_sales.domains.input;

import lombok.Data;

@Data
public class ActivityInput {
    private String title;
    private String description;
    private String location;
    private String activityDate;
    private int leadId;
}
