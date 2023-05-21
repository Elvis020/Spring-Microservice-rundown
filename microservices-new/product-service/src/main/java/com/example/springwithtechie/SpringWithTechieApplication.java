package com.example.springwithtechie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringWithTechieApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWithTechieApplication.class, args);
    }

}
