package br.com.comigo.gateway.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import reactor.core.publisher.Mono;

@Slf4j
@ConfigurationProperties(prefix = "yc.security")
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final SecurityRules securityRules;
    private List<String> publicPaths;

    public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths;
    }

    public List<String> getPublicPaths() {
        return this.publicPaths;
    }

    public JwtAuthenticationFilter(
            @Value("${yc.security.keys.accessToken.secret}") String secret,
            SecurityRules securityRules) {
        this.algorithm = Algorithm.HMAC512(secret);
        this.verifier = JWT.require(this.algorithm)
                .withIssuer("Comigo Auth-Service")
                .build();
        this.securityRules = securityRules;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (this.publicPaths == null) {
            log.warn("Public paths is null! Check configuration.");
            return chain.filter(exchange);
        }

        String path = exchange.getRequest().getPath().value();
        if (this.isPublicPath(path)) {
            log.debug("Allowing public path access");
            return chain.filter(exchange);
        }
        
        log.info(" > path: {}", path);

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        
        log.info(" > authHeader: {}", authHeader);

        try {
            DecodedJWT jwt = this.verifier.verify(authHeader.substring(7));
            
            log.info(" > jwt.token: {}", jwt.getToken());

            List<String> roles = this.extractRoles(jwt);
            log.info(" > [out] roles: {}", roles);

            if (!securityRules.isAuthorized(path, roles)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            log.info(" > authorized");

            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("JWT Authentication failed", e);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private List<String> extractRoles(DecodedJWT jwt) {
        String roles = jwt.getClaim("roles").asString();
        log.info(" > roles: {}", roles);
        return Arrays.asList(roles.replaceAll("[\\[\\]\"]", "").split(","));
    }

    private boolean isPublicPath(String path) {
        if (this.publicPaths == null) {
            return false;
        }
        return this.publicPaths.stream()
                .anyMatch(publicPath -> {
                    boolean result = path.contains(publicPath);
                    log.debug("Checking if {} contains {}: {}", path, publicPath, result);
                    return result;
                });
    }

    @Override
    public int getOrder() {
        return -100;
    }
}