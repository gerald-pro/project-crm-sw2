package com.jonatan.dev.crm_sales.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "sale_details")
public class SaleDetail {
    @Id
    private String id;
    private String invoiceNo;
    private LocalDateTime invoiceDate;
    private String description;
    private Float unitPrice;
    private Integer quantity;
    private String country;
    private int contactId;
    private String productId;
}
