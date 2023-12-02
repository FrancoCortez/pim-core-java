package com.example.pimcoreapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PimCoreApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PimCoreApiApplication.class, args);
    }

}
