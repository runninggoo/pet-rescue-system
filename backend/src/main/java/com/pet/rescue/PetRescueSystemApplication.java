package com.pet.rescue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pet.rescue.mapper")
public class PetRescueSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetRescueSystemApplication.class, args);
    }
}