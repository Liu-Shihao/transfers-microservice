package com.lsh.account.transfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.lsh.account.transfer.client")
public class AccountTransfersApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountTransfersApplication.class, args);
    }
} 