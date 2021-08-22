package demo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@SpringBootApplication
public class JpaDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaDemoApplication.class, args);
    }

    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void setUp() {
        // display datasource info
        try (Connection conn = dataSource.getConnection()) {
            logger.info("// ======================================================");
            logger.info("## Check datasource : {}", dataSource);

            DatabaseMetaData metaData = conn.getMetaData();

            String productName = (String) DatabaseMetaData.class
                    .getMethod("getDatabaseProductName")
                    .invoke(metaData);

            logger.info("> url : {}", metaData.getURL());
            logger.info("> username : {}", metaData.getUserName());
            logger.info("> product name : {}", productName);
            logger.info("========================================================= //");
        } catch (Exception e) {
            logger.warn("Exception occur while checking datasource {}", e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity checkHealth() {
        return ResponseEntity.ok().build();
    }
}
