package br.com.comigo.gateway.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
public class WebCorsConfig {

    Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ALLOWED_HEADERS_EXPOSE = "xsrf-token";
    private static final String CREDENTIALS = "true";
    private static final String ALLOWED_HEADERS = "*";
    private static final String ALLOWED_METHODS = "POST, GET, PUT, DELETE, OPTIONS";

    private static final String MAX_AGE = "3600"; // 2 hours (2 * 60 * 60)

    @Bean
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();

            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();

                headers.add("Access-Control-Allow-Credentials", CREDENTIALS);
                headers.add("Access-Control-Expose-Headers", ALLOWED_HEADERS_EXPOSE);
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                headers.add("Access-Control-Max-Age", MAX_AGE);

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    headers.add("Access-Control-Allow-Origin", request.getHeaders().getOrigin());
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }

            return chain.filter(ctx);
        };
    }
}