package br.com.comigo.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaStartup {
	public static void main(String[] args) {
		SpringApplication.run(EurekaStartup.class, args);
	}
}
