package br.com.comigo.gateway.service;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

//@Component
public class BodyCatcher implements RewriteFunction<String, String>
{
    Logger logger = LoggerFactory.getLogger(getClass());

    static String responseMessage = "done!";

    @Override
    public Publisher<String> apply(ServerWebExchange exchange, String requestMessage)
    {
        if (requestMessage == null) {
            throw new RuntimeException("400:the request body can not be null.");
        }

        //HttpHeaders headers = exchange.getRequest().getHeaders();

        return Mono.just(responseMessage);
    }
}
