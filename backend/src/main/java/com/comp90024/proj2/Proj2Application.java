package com.comp90024.proj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Proj2Application {

    @RequestMapping("/")
    public String home() {
        return "Welcome";
    }

    public static void main(String[] args) {
        SpringApplication.run(Proj2Application.class, args);
    }

}
