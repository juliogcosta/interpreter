package br.com.comigo.assistencia.adapter.outbount.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.comigo.common.model.records._assistencia.ClienteDTO;

@FeignClient(name = "${app.assistencia.name}")
public interface AssistenciaRestClient {

    @GetMapping("/unsec/cliente/{id}")	
	public ClienteDTO findClienteById(@PathVariable Long id) throws Exception;

    @GetMapping("/unsec/prestador/servicoDePrestador/{id}")	
	public ResponseEntity<String> findServicoDePrestadorById(@PathVariable String id) throws Exception;

    @GetMapping("/unsec/prestador/servicoDePrestador/{ids}/s")	
	public ResponseEntity<String> findServicoDePrestadorsByIds(@PathVariable String ids) throws Exception;
}
