package com.sas.ims;

import com.sas.tokenlib.config.CommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages = {"com.sas.*","com.sas.tokenlib.*"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Import(CommonConfig.class)
@EnableJpaRepositories("com.sas.ims")
public class ImsmasterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImsmasterApplication.class, args);
	}

}
