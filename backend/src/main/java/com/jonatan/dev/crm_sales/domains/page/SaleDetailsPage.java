package com.jonatan.dev.crm_sales.domains.page;

import com.jonatan.dev.crm_sales.domains.SaleDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailsPage {
    private List<SaleDetail> salesDetails;
    private int totalCount;
    private int totalPages;
}
