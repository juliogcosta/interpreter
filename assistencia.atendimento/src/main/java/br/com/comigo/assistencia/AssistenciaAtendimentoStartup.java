package br.com.comigo.assistencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@SpringBootApplication
@ComponentScan
@EntityScan
@EnableJpaRepositories
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {
        "br.com.comigo.core"
})
public class AssistenciaAtendimentoStartup {

    public static void main(String[] args) {
        SpringApplication.run(AssistenciaAtendimentoStartup.class, args);
    }
}
