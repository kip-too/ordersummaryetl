package com.northwind.ordersummaryetl.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderSummaryDTO {
    private Integer orderId;
    private String customerId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status;
}
