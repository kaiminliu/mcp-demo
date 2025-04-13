package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class McpServer2FeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServer2FeignApplication.class, args);
    }

}
