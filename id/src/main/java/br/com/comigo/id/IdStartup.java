package br.com.comigo.id;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages = {
		"br.com.comigo.id.**",
		"com.yc.core.common.infrastructure.exception.util"
})
public class IdStartup {
	public static void main(String[] args) {
		SpringApplication.run(IdStartup.class, args);
	}
}
