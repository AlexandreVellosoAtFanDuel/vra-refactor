package com.betfair.video.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VideoRetrievalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoRetrievalApiApplication.class, args);
	}

}
