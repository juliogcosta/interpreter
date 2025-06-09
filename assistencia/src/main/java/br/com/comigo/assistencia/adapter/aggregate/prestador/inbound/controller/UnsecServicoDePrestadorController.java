package br.com.comigo.assistencia.adapter.aggregate.prestador.inbound.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorResponse;
import br.com.comigo.assistencia.application.aggregate.service.prestador.ServicoDePrestadorServiceImpl;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/unsec/prestador")
public class UnsecServicoDePrestadorController {
    private final ServicoDePrestadorServiceImpl servicoDePrestadorService;

    @GetMapping("/servicoDePrestador/{id}")
    public ResponseEntity<ServicoDePrestadorResponse> getServicoDePrestadorDetailsById(@PathVariable String id) 
    		throws ControlledException {
    	ServicoDePrestadorResponse servicoDePrestadorResponse = this.servicoDePrestadorService.getServicoDePrestadorDetailsById(UUID.fromString(id), true);
        return ResponseEntity.ok(servicoDePrestadorResponse);
    }

	@GetMapping("/servicoDePrestador/{ids}/s")
	public ResponseEntity<List<ServicoDePrestadorResponse>> getServicoDePrestadorsDetailsByIds(@PathVariable List<String> ids) 
			throws ControlledException {
		List<ServicoDePrestadorResponse> servicoDePrestadorResponses = this.servicoDePrestadorService
				.getServicoDePrestadorDetailsByIds(ids.stream().map(id -> UUID.fromString(id)).toList(), true);
		return ResponseEntity.ok(servicoDePrestadorResponses);
	}
}
