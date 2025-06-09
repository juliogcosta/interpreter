package br.com.comigo.gateway;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import br.com.comigo.gateway.infrastructure.config.SecurityRulesConfig;

@EnableDiscoveryClient
@EnableConfigurationProperties(SecurityRulesConfig.class)
@SpringBootApplication()
public class GatewayStartup implements CommandLineRunner {

    @Value("${yc.api.gateway.cache.request-limit.ttl}")
    private Integer ttl;

    @Value("${yc.api.management.log.trace.enable}")
    private Boolean lt = false;

    public static Boolean monitorNotFoundError = false;

    final static public Map<String, JSONArray> monitorNotFoundErrorMap = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(GatewayStartup.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
