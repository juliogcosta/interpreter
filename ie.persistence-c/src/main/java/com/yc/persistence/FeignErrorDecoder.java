package com.yc.persistence;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;

@Component
public class FeignErrorDecoder implements ErrorDecoder
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Exception decode(String methodKey, Response response)
    {
        try
        {
            String from = response.request().url().split(Pattern.quote("/"))[2];
            String errorFromExt = null;
            if (response.body() == null)
            {
                errorFromExt = "{\"message\":\"no message from previous microservice\"}";
            }
            else
            {
                errorFromExt = new String(
                        response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }

            JSONObject jsErrorFromExt = new JSONObject(errorFromExt);
            String message = "";
            if (jsErrorFromExt.has("message")) 
            {
                message = jsErrorFromExt.getString("message");
            }
            else message = "[".concat(errorFromExt).concat("]");

            return new Exception(String.valueOf(response.status()).concat(":").concat(message).concat(" [").concat(from == null ? "?" : from).concat("][EXT]"));
        }
        catch (Exception e)
        {
            throw new RuntimeException("error on trying to error-decode from previous microservice.", e);
        }
    }
}
