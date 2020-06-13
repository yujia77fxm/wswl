package com.wswl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.wswl.dao")
@SpringBootApplication
public class WalletBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(WalletBootApplication.class, args);
    }
}
