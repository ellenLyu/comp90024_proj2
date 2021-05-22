package com.comp90024.proj2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
@EnableCaching
public class Proj2Application {

    @RequestMapping("/")
    public String home() {
        return "Welcome";
    }

    public static void main(String[] args) {
        SpringApplication.run(Proj2Application.class, args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        System.out.println("TaskScheduler");
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10);
        return taskScheduler;
    }
}
