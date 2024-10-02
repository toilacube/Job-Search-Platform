package com.training.lehoang.modules.auth;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OtpCacheConfig {

    @Bean
    public LoadingCache<String, Integer> otpCache() {
        final var expirationMinutes = 200000;
        return CacheBuilder.newBuilder().expireAfterWrite(expirationMinutes, TimeUnit.MINUTES)
                .build(new CacheLoader<>() {
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }
}
