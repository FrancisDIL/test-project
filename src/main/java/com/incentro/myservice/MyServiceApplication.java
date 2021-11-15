package com.incentro.myservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
@EnableResourceServer
@EnableScheduling
@EnableRetry
public class MyServiceApplication {

    @Value("${myservice.timezone}")
    private String timeZone;

    @Autowired
    com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    public static void main(String[] args) {
        SpringApplication.run(MyServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
        System.out.println("Post Construct Spring boot application running in " + ZoneId.systemDefault() + " timezone :" + new Date());
        objectMapper.setTimeZone(TimeZone.getTimeZone(timeZone));
    }

}
