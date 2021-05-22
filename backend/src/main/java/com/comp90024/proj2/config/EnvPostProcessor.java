package com.comp90024.proj2.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class EnvPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("jasypt.encryptor.password", "comp90024");
        MapPropertySource mapPropertySource = new MapPropertySource("encryptor", properties);
        environment.getPropertySources().addFirst(mapPropertySource);
    }

}
