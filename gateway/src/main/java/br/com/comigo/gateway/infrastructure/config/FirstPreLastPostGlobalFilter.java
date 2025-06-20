package br.com.comigo.gateway.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class FirstPreLastPostGlobalFilter
  implements GlobalFilter, Ordered {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
      GatewayFilterChain chain) {
        return chain.filter(exchange)
          .then(Mono.fromRunnable(() -> logger.info("Last Post Global Filter")));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}