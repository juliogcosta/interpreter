package br.com.comigo.assistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {
		//"com.yc.core.common.infrastructure.exception.util"
})
public class AssistenciaStartup {
	public static void main(String[] args) {
		SpringApplication.run(AssistenciaStartup.class, args);
	}
}
