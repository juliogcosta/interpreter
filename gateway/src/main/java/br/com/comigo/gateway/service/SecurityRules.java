package br.com.comigo.gateway.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import br.com.comigo.gateway.infrastructure.config.SecurityRulesConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityRules {
    private final Map<String, List<String>> mappedPathToRoles;

    public SecurityRules(SecurityRulesConfig securityRulesConfig) {
        this.mappedPathToRoles = securityRulesConfig.getPathRolesMap();
        log.info("Regras de segurança carregadas: {}", this.mappedPathToRoles);
    }

    public boolean isAuthorized(String path, List<String> userRoles) {
        log.info(" > userRoles: {}", userRoles);
        return this.mappedPathToRoles.entrySet().stream()
                .filter(pathRole -> {
                    String mappedPath = pathRole.getKey();
                    log.info(" > {}.startsWith({}): {}", path, mappedPath, path.startsWith(mappedPath));
                    return path.startsWith(mappedPath);
                })
                .findFirst()
                .map(pathRole -> {
                    List<String> toRoles = pathRole.getValue();
                    log.info(" > toRoles: {}", toRoles);
                    return toRoles.stream().anyMatch(userRoles::contains);
                })
                .orElse(false);
    }
}