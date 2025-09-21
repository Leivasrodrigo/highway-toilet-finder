package com.highwaytoiletfinder.common.security;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.setTaskDecorator(runnable -> {
            String correlationId = MDC.get("correlationId");
            return () -> {
                if (correlationId != null) {
                    MDC.put("correlationId", correlationId);
                }
                try {
                    runnable.run();
                } finally {
                    if (correlationId != null) {
                        MDC.remove("correlationId");
                    }
                }
            };
        });
        executor.initialize();

        return new DelegatingSecurityContextAsyncTaskExecutor(executor);
    }
}
