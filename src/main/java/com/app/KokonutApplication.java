package com.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
public class KokonutApplication {

    public static void main(String[] args) {

        SpringApplication.run(KokonutApplication.class, args);
    }

}