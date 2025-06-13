package com.yc.core.common.infrastructure.config;

import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            /**
             * String from = response.request().url().split(Pattern.quote("/"))[2];
             * 
             */
            String originalMessage = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return new Exception(String.valueOf(response.status()).concat(":").concat(originalMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
