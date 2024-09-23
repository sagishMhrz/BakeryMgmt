package com.project.bakerymanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BakeryManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run( BakeryManagementSystemApplication.class, args);
    }

}


