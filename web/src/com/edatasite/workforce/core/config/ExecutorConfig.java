package com.edatasite.workforce.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CORE_POOL_SIZE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.MAX_POOL_SIZE;

@Configuration
public class ExecutorConfig {
    int cores = Math.max(2, Runtime.getRuntime().availableProcessors());

    private final ExecutorService executorService = new ThreadPoolExecutor(
            cores,
            64,
            30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Bean
    public ExecutorService executorService() {
        return executorService;
    }

    @PreDestroy
    public void shutdownExecutor() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
