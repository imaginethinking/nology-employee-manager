package com.nology.employeemanager.config.seeder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
@Component
@Profile("dev") 
public class DatabaseSeeder implements CommandLineRunner {
    
    private static final Logger log = LogManager.getLogger(DatabaseSeeder.class);

    private final EmployeeSeeder employeeSeeder;
    private final ContractSeeder contractSeeder;

    @Override
    public void run(String... args) {
        if (employeeSeeder.isRepositoryEmpty()) {
            log.info("Seeding 50 employees...");
            employeeSeeder.seed(50);
            log.info("Done");
        }

        if (contractSeeder.isRepositoryEmpty()) {
            log.info("Seeding 75 contracts...");
            contractSeeder.seed(75);
            log.info("Done");
        }
    }
}
