package com.trillo.innovesync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class InnovesyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(InnovesyncApplication.class, args);
    }

}
