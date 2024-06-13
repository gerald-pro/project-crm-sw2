package com.jonatan.dev.crm_sales.domains.page;

import com.jonatan.dev.crm_sales.domains.Lead;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadsPage {
    private List<Lead> leads;
    private int totalCount;
    private int totalPages;
}
