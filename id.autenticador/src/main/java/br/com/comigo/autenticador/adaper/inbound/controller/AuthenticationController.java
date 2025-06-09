package br.com.comigo.autenticador.adaper.inbound.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.comigo.autenticador.adaper.dto.AuthrorizationResponse;
import br.com.comigo.autenticador.adaper.dto.Credential;
import br.com.comigo.autenticador.domain.User;
import br.com.comigo.autenticador.infrastructure.security.JwtUtils;
import br.com.comigo.common.infrastructure.exception.RegisterNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthenticationController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping(path = "/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@RequestBody Credential credential) throws RegisterNotFoundException, BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        user.setStatus(user.getStatus());
        String jwt = jwtUtils.generateAccessToken(user);

        List<String> roles = user.getAuthorities().stream().map(item -> {
            return item.getAuthority();
        }).collect(Collectors.toList());

        AuthrorizationResponse jwtResponse = new AuthrorizationResponse(jwt, user.getUsername(), user.getName(), user.getEmail().email(), roles);
        return ResponseEntity.ok(jwtResponse);
    }
}