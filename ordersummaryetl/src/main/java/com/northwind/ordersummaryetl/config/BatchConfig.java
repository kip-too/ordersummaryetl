package com.northwind.ordersummaryetl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;



import com.northwind.ordersummaryetl.dto.OrderSummaryDTO;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class BatchConfig {
    // reader: extract data from northwind orders & order details
    @Bean
    public JdbcCursorItemReader<OrderSummaryDTO> reader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<OrderSummaryDTO>()
                .name("orderReader")
                .dataSource(dataSource)
                .sql("""
                                             select
                        o.order_id,o.customer_id,o.order_date,
                        sum(od.unit_price * od.quantity * (1-od.discount)) as total_amount,
                        case
                           when o.shipped_Date is not null then 'SHIPPED'
                           ELSE 'PENDING'
                        end as status
                        from orders o
                        inner join order_details od
                        on o.order_id = od.order_id
                        group by o.order_id,o.customer_id,o.order_date,o.shipped_date
                        order by o.order_id
                                             """)
                .rowMapper(this::mapRow)
                .fetchSize(100)
                .build();
    }

    // writer: load data into order_summary table
    @Bean
    public JdbcBatchItemWriter<OrderSummaryDTO> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<OrderSummaryDTO>()
                .dataSource(dataSource)

                .sql("""
                        insert into order_summary (order_id, customer_id, order_date, total_amount, status)
                        values (:orderId, :customerId, :orderDate, :totalAmount, :status)
                        on conflict (order_id) do update set
                        total_amount = EXCLUDED.total_amount,
                        status = EXCLUDED.status,
                        processed_at = current_timestamp
                        """)
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())

                .build();
    }

    //step = reader + processor + writer
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
        JdbcCursorItemReader<OrderSummaryDTO> reader,
        ItemProcessor<OrderSummaryDTO, OrderSummaryDTO> processor,
        JdbcBatchItemWriter<OrderSummaryDTO> writer
    ){
        return new StepBuilder("orderSummaryStep",jobRepository)
        .<OrderSummaryDTO,OrderSummaryDTO>chunk(100,transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
    }
    //job 
    @Bean 
    public Job orderSummaryJob(JobRepository jobRepository, Step step1){
        return new JobBuilder("orderSummaryJob",jobRepository)
                .start(step1)
                .build();
    }

    // helper method to map ResultSet to OrderSummaryDTO
    private OrderSummaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderSummaryDTO dto = new OrderSummaryDTO();
        dto.setOrderId(rs.getInt("order_id"));
        dto.setCustomerId(rs.getString("customer_id"));
        dto.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
        dto.setTotalAmount(rs.getBigDecimal("total_amount"));
        dto.setStatus(rs.getString("status"));
        return dto;
    }
}
