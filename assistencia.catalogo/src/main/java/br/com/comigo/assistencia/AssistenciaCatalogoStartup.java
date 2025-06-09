package br.com.comigo.assistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {
        "br.com.comigo.core"
})
public class AssistenciaCatalogoStartup {

    public static void main(String[] args) {
        SpringApplication.run(AssistenciaCatalogoStartup.class, args);
    }
}
