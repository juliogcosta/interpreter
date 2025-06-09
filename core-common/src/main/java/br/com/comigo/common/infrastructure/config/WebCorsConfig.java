package br.com.comigo.common.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCorsConfig  implements WebMvcConfigurer {

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) 
            {
                registry.addMapping("/**")
                    .allowCredentials(true)
                    .allowedOriginPatterns("*")
                    .allowedHeaders(
                            HttpHeaders.ORIGIN,
                            HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS,
                            HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                            HttpHeaders.AUTHORIZATION,
                            HttpHeaders.CONTENT_TYPE,
                            HttpHeaders.ACCEPT)
                    .allowedMethods(
                            HttpMethod.POST.name(), 
                            HttpMethod.GET.name(), 
                            HttpMethod.PUT.name(), 
                            HttpMethod.DELETE.name(), 
                            HttpMethod.OPTIONS.name());
            }
        };
    }
}