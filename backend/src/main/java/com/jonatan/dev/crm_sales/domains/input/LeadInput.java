package com.jonatan.dev.crm_sales.domains.input;

import com.jonatan.dev.crm_sales.domains.LeadStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadInput {
    private String title;
    private String description;
    private String userId;
    private String contactId;
    private Float value;
    private LeadStatus status = LeadStatus.nuevo;
}
