package br.com.comigo.gateway.infrastructure.config;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import br.com.comigo.common.utils.HashUtils;
import reactor.core.publisher.Mono;

@Component
public class URLGlobalPreFilter implements GlobalFilter, Ordered {
    final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getURI().getPath().contains("/unsec/")) {
            throw new RuntimeException("URL Not Found.");
        } else {
            String hash = new HashUtils().generateHash(UUID.randomUUID().toString(), 12);
            ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                    .header("x-b3-traceid", hash)
                    .header("x-b3-spanid", hash.concat(":spanid"))
                    .header("x-b3-parentspanid", hash.concat(":parentspanid"))
                    .build();
            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
