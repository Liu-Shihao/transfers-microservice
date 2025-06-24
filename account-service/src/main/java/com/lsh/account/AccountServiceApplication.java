package com.lsh.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Spring Cloud 2020 及以上版本可以省略 @EnableEurekaClient，只要有依赖和配置即可自动注册。
// Swagger UI: http://localhost:8083/swagger-ui/index.html
@SpringBootApplication
public class AccountServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }
} 