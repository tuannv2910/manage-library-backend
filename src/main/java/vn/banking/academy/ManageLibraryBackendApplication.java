package vn.banking.academy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ManageLibraryBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageLibraryBackendApplication.class, args);
    }
}
