package br.com.comigo.common.infrastructure.config;

import feign.Contract;
import feign.RequestInterceptor;

import java.lang.System.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            /*
             * requestTemplate.header("Accept", "application/json");
             * 
             */
            requestTemplate.header("X-Custom-Header", "custom-value");
        };
    }
    
    // Para habilitar logging detalhado
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.ALL;
    }
    
    @Bean
    Contract feignContract() {
        return new feign.Contract.Default();
    }
}