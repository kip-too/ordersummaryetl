# Order Summary ETL Job

A  **Spring Batch** application designed to automate the extraction, transformation, and loading (ETL) of sales data. This job pulls raw order data from a Northwind PostgreSQL instance, calculates total order amounts (including discounts), and persists the results into a dedicated `order_summary` table.



##  ETL Workflow
1.  **Extract:** Reads records from the `orders` and `order_details` tables in the Northwind database.
2.  **Transform:** Aggregates line items, applies unit prices, and calculates the final total per order.
3.  **Load:** Inserts the calculated summaries into the `order_summary` destination table.

---

##  Prerequisites
* **Java:** 17 or higher
* **Build Tool:** Maven 3.6+
* **Database:** PostgreSQL with the Northwind schema.
    * *Source Schema:* [postgres-normalized-northwind](https://github.com/peterldowns/postgres-normalized-northwind)

---

##  Configuration
Before running the job, update your `src/main/resources/application.properties` with your database credentials:

```properties
# Database Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/northwind
spring.datasource.username=your_username
spring.datasource.password=your_password

# Spring Batch Settings
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=always

---
## Dependencies
This Project utilizes this dependencies:
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jdbc</artifactId>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
