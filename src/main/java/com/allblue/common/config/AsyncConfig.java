package com.allblue.common.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncConfig {

    private static final int CORE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 50;
    private static final int QUEUE_CAPACITY = 100;
    private static final String THREAD_NAME_PREFIX = "Worker-Async-";
    private static final int INSPECTION_POOL_SIZE = 3;
    private static final int INSPECTION_QUEUE_CAPACITY = 500;
    private static final String INSPECTION_THREAD_NAME_PREFIX = "Inspection-";
    private static final int LOOKBOOK_POOL_SIZE = 5;
    private static final int LOOKBOOK_QUEUE_CAPACITY = 200;
    private static final String LOOKBOOK_THREAD_NAME_PREFIX = "Lookbook-AI-";

    @Bean(name = "workerTaskExecutor")
    public Executor workerTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }

    @Bean(name = "inspectionTaskExecutor")
    public Executor inspectionTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(INSPECTION_POOL_SIZE);
        executor.setMaxPoolSize(INSPECTION_POOL_SIZE);
        executor.setQueueCapacity(INSPECTION_QUEUE_CAPACITY);
        executor.setThreadNamePrefix(INSPECTION_THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(120);

        executor.initialize();
        return executor;
    }

    @Bean(name = "lookbookTaskExecutor")
    public Executor lookbookTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(LOOKBOOK_POOL_SIZE);
        executor.setMaxPoolSize(LOOKBOOK_POOL_SIZE);
        executor.setQueueCapacity(LOOKBOOK_QUEUE_CAPACITY);
        executor.setThreadNamePrefix(LOOKBOOK_THREAD_NAME_PREFIX);
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(120);

        executor.initialize();
        return executor;
    }
}
