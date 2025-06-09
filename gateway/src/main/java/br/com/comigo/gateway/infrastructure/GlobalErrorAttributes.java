package br.com.comigo.gateway.infrastructure;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import br.com.comigo.gateway.GatewayStartup;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes 
{
    static final Logger logger = LoggerFactory.getLogger(GlobalErrorAttributes.class);

    public GlobalErrorAttributes() {
    }

    public GlobalErrorAttributes(boolean includeException) {
    }

    @SuppressWarnings("null")
    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = this.getError(request);
        
        String address = null;
        if (request.exchange().getRequest().getRemoteAddress() == null
            || request.exchange().getRequest().getRemoteAddress().getAddress() == null
            || request.exchange().getRequest().getRemoteAddress().getAddress().getHostAddress() == null) {
            throw new RuntimeException("RemoteAddress is null");
        } else address = request.exchange().getRequest().getRemoteAddress().getAddress().getHostAddress();

        if (GatewayStartup.monitorNotFoundError) {
            if (GatewayStartup.monitorNotFoundErrorMap.containsKey(address)) {
                
            } else {
                GatewayStartup.monitorNotFoundErrorMap.put(address, new JSONArray());
            }

            JSONObject jsStat = new JSONObject();
            jsStat.put("path", request.exchange().getRequest().getPath().value());
            jsStat.put("method", request.exchange().getRequest().getMethod());
            GatewayStartup.monitorNotFoundErrorMap.get(address).put(jsStat);
        }

        MergedAnnotation<ResponseStatus> responseStatusAnnotation = MergedAnnotations
                .from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                .get(ResponseStatus.class);
        HttpStatus errorStatus = findHttpStatus(error, responseStatusAnnotation);

        Map<String, Object> map = super.getErrorAttributes(request, options);
        map.remove("timestamp");
        map.remove("path");
        map.remove("error");
        map.remove("requestId");
        map.put("statusCode", errorStatus);
        map.put("errorCode", getErrorCode(map, errorStatus));

        return map;
    }

    private HttpStatus findHttpStatus(Throwable error, MergedAnnotation<ResponseStatus> responseStatusAnnotation) {
        if (error instanceof ResponseStatusException) {
            return (HttpStatus) ((ResponseStatusException) error).getStatusCode();
        }
        return responseStatusAnnotation.getValue("code", HttpStatus.class).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getErrorCode(Map<String, Object> map, HttpStatus errorStatus) {
        String errorCode;
        switch (errorStatus) {
            case NOT_FOUND:
                errorCode = "404 Not Found";
                map.put("message", "The url is not found");
                break;
            case METHOD_NOT_ALLOWED:
                errorCode = "405 Method Not Allowed";
                map.put("message", "Invalid HTTP Method type for the url");
                break;
            default:
                errorCode = "500 Internal Server Error";
                map.put("message", "Unexpected Error");
        }
        return errorCode;
    }
}