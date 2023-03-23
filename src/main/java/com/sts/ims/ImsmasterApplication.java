package com.sts.ims;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ImsmasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImsmasterApplication.class, args);
	}

}
