package com.sicredi.libraryhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LibraryHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryHubApplication.class, args);
    }

}
