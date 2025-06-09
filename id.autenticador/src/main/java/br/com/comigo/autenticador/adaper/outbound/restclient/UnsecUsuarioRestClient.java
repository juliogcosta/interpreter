package br.com.comigo.autenticador.adaper.outbound.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${app.id.name}", url = "${app.id.address}:${app.id.port}", path = "/unsec")
public interface UnsecUsuarioRestClient {
    @GetMapping("/username/{username}/to-login")
    public ResponseEntity<String> getUsuario(@PathVariable String username);
}