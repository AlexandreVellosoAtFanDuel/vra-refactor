package com.betfair.video.api;

import com.betfair.video.api.infra.consumer.ConfigurationItemCacheManager;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.ExecutorService;

@SpringBootApplication
@EnableFeignClients
public class VideoRetrievalApiApplication {

    private final ApplicationContext applicationContext;

    private final ExecutorService executor;

    public VideoRetrievalApiApplication(ApplicationContext applicationContext, ExecutorService executor) {
        this.applicationContext = applicationContext;
        this.executor = executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(VideoRetrievalApiApplication.class, args);
    }

    /**
     * This method initiate the Kafka consumers to revalidate the caches
     */
    @PostConstruct
    public void atStartup() {
        ConfigurationItemCacheManager configurationItemCacheManager = applicationContext.getBean(ConfigurationItemCacheManager.class);

        executor.submit(configurationItemCacheManager);
    }

}
