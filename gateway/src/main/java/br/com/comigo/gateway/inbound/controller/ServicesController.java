package br.com.comigo.gateway.inbound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServicesController {
    
    @GetMapping("/fallback")
    public ResponseEntity<String> errorFallback() {
        return ResponseEntity.internalServerError().body("We are facing a problem. Please contact support.");
    }

}