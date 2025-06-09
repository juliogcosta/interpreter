package br.com.comigo.assistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {
		//"br.com.comigo.common.infrastructure.exception.util"
})
public class AssistenciaStartup {
	public static void main(String[] args) {
		SpringApplication.run(AssistenciaStartup.class, args);
	}
}
