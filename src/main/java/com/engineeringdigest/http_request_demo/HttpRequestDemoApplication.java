package com.engineeringdigest.http_request_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HttpRequestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpRequestDemoApplication.class, args);
	}

}
