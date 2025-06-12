package com.yc.persistence.c;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = { "com.yc.persistence.c", "br.com.comigo.common", "com.yc.core" })
public class PersistenceCStartup {

	public static void main(String[] args) {
		SpringApplication.run(PersistenceCStartup.class, args);
	}
}
