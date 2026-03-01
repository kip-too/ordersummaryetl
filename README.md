# Order Summary ETL Job
This job extracts order data from NorthWind Postgresql databse ,transorms it by calculating total amounts
and loading it into a new order_summary table.
## Prerequisites
1. Java 17+
2. Maven
3. PostgrSQL database with northwind schema loaded.
 This can be found here [Display Text](peterldowns/postgres-normalized-northwind)

## Dependencies
<pre>
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-batch</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Utilities -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
  
</pre>
