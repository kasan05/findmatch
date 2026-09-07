package com.matrimony.findmatch.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean(name = "appExecutorService")
    public ExecutorService appExecutorService(){
        return Executors.newVirtualThreadPerTaskExecutor();
    }
    @Bean
    public CacheManager cacheManager() {
        // Creates a basic, in-memory concurrent map cache manager
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .maximumSize(1000));
        return cacheManager;
    }
//    @Bean
//    public JvmMemoryMetrics jvmMemoryMetrics(MeterRegistry registry) {
//        JvmMemoryMetrics jvmMetrics = new JvmMemoryMetrics();
//        jvmMetrics.bindTo(registry);
//        return jvmMetrics;
//    }
}
