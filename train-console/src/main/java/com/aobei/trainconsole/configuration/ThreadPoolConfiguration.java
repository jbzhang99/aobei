package com.aobei.trainconsole.configuration;

import com.aobei.trainconsole.exception.UncaughtException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * create by renpiming on 2018/2/2
 * 线程池生产基地，
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean
    ExecutorService threadPool( ThreadFactory threadFactory){
        return Executors.newFixedThreadPool(20,threadFactory);
    }

    @Bean
    CompletionService completionService(ExecutorService threadPool){
        return new ExecutorCompletionService(threadPool);
    }

    @Bean
    ThreadFactory threadFactory(){
        ThreadFactory factory = r -> {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new UncaughtException());
            return t;
        };
        return factory;
    }

}
