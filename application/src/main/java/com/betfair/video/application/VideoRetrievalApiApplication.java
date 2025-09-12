package com.betfair.video.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.betfair.video")
public class VideoRetrievalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideoRetrievalApiApplication.class, args);
    }

}
