package com.sicredi.libraryhub.infrastructure.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {
    public static final String CACHE_LIVRO = "biblioteca:livro";
}
