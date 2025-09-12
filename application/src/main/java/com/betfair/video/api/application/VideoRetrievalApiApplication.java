package com.betfair.video.api.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.betfair.video.api")
public class VideoRetrievalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoRetrievalApiApplication.class, args);
    }

}
