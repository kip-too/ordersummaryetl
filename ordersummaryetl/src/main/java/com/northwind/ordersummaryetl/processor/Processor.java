package com.northwind.ordersummaryetl.processor;

import java.math.RoundingMode;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import com.northwind.ordersummaryetl.dto.OrderSummaryDTO;

@Configuration
public class Processor {
        // processor: transform /validate
        @Bean
    public ItemProcessor<OrderSummaryDTO, OrderSummaryDTO> orderSummaryProcessor() {
        return item -> {
            if (item.getTotalAmount() !=  null){
                item.setTotalAmount(item.getTotalAmount().setScale(2,RoundingMode.HALF_UP));
            }
            return item;
        };
    }
}
