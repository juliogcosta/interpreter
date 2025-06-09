package br.com.comigo.assistencia.adapter.inbound.aggregate.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.comigo.core.error.AggregateStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(value = { AggregateStateException.class })
    protected ResponseEntity<JsonNode> handleException(AggregateStateException aggregateStateException) {
        return ResponseEntity.badRequest()
                .body(objectMapper.createObjectNode().put("error", aggregateStateException.getMessage()));
    }
}
