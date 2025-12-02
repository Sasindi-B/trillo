package com.trillo.innovesync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
        exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.trillo.innovesync", "com.trillo.app"})
public class InnovesyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnovesyncApplication.class, args);
    }

}
