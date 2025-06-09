package br.com.comigo.assistencia.adapter.aggregate.prestador.inbound.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorResponse;
import br.com.comigo.assistencia.application.aggregate.service.prestador.ServicoDePrestadorServiceImpl;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;
import br.com.comigo.common.infrastructure.exception.BusinessRuleConsistencyException;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prestador/{prestadorId}")
public class ServicoDePrestadorController {
	private final ServicoDePrestadorServiceImpl servicoDePrestadorService;

	@PostMapping("/servicoDePrestador")
	public ResponseEntity<ServicoDePrestadorDTO> create(@PathVariable Long prestadorId,
			@Valid @RequestBody ServicoDePrestadorDTO servicoDePrestadorDTO)
			throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
		ServicoDePrestadorDTO servicoDePrestador = this.servicoDePrestadorService.create(servicoDePrestadorDTO,
				prestadorId);
		return ResponseEntity.ok(servicoDePrestador);
	}

	@PutMapping("/servicoDePrestador")
	public ResponseEntity<ServicoDePrestadorDTO> update(@PathVariable Long prestadorId,
			@Valid @RequestBody ServicoDePrestadorDTO servicoDePrestadorDTO)
			throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
		this.servicoDePrestadorService.update(servicoDePrestadorDTO, prestadorId);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/servicoDePrestador")
	public ResponseEntity<List<ServicoDePrestadorResponse>> getServicoDePrestadors(@PathVariable Long prestadorId)
			throws ControlledException {
		return ResponseEntity
				.ok(this.servicoDePrestadorService.getAllServicoDePrestadorByPrestador(prestadorId, false));
	}

	@GetMapping("/servicoDePrestador/{id}")
	public ResponseEntity<ServicoDePrestadorResponse> getServicoDePrestadorDetailsById(@PathVariable Long prestadorId,
			@PathVariable String id) throws ControlledException {
		ServicoDePrestadorResponse servicoDePrestadorResponse = this.servicoDePrestadorService
				.getServicoDePrestadorDetailsById(UUID.fromString(id), prestadorId, false);
		return ResponseEntity.ok(servicoDePrestadorResponse);
	}

	@GetMapping("/servicoDePrestador/{ids}/s")
	public ResponseEntity<List<ServicoDePrestadorResponse>> getServicoDePrestadorDetailsByIds(
			@PathVariable Long prestadorId, @PathVariable List<String> ids) throws ControlledException {
		List<ServicoDePrestadorResponse> servicoDePrestadorResponses = this.servicoDePrestadorService
				.getServicoDePrestadorDetailsByIds(ids.stream().map(id -> UUID.fromString(id)).toList(), prestadorId,
						false);
		return ResponseEntity.ok(servicoDePrestadorResponses);
	}

	@GetMapping("/servicoDePrestador/status/{status}")
	public ResponseEntity<List<ServicoDePrestadorResponse>> getFilteredServicoDePrestadorDetailsByStatus(
			@PathVariable Long prestadorId, @PathVariable String status) throws ControlledException {
		List<ServicoDePrestadorResponse> prestadorDTOs = this.servicoDePrestadorService
				.getFilteredServicoDePrestadorDetailsByStatus(Status.valueOf(status.toUpperCase()), prestadorId, false);
		return ResponseEntity.ok(prestadorDTOs);
	}

	@DeleteMapping("/servicoDePrestador/{id}")
	public ResponseEntity<Void> getPrestadorsByTelefone(@PathVariable Long prestadorId, @PathVariable String id) {
		this.servicoDePrestadorService.deleteServicoDePrestador(UUID.fromString(id), prestadorId);
		return ResponseEntity.ok().build();
	}
}
