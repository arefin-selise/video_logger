package com.dev.vlogger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.dev.vlogger.config.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
public class VloggerApplication {

	public static void main(String[] args) {
		SpringApplication.run(VloggerApplication.class, args);
	}

}
