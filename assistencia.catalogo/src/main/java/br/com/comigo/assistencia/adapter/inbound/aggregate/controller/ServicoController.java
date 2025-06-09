package br.com.comigo.assistencia.adapter.inbound.aggregate.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.ServicoDTO;
import br.com.comigo.assistencia.adapter.outbount.projection.JpaServicoProjection;
import br.com.comigo.assistencia.adapter.outbount.projection.respository.JpaServicoProjectionRepository;
import br.com.comigo.assistencia.domain.aggregate.command.AjustarServicoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.CancelarServicoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.DisponibilizarServicoCommand;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.core.application.service.command.CommandProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/servico")
@RequiredArgsConstructor
public class ServicoController {

    private final ObjectMapper objectMapper;
    private final CommandProcessor commandProcessor;
    private final JpaServicoProjectionRepository jpaServicoProjectionRepository;

    @PostMapping
    public ResponseEntity<JsonNode> disponibilizarServico(@RequestBody ServicoDTO servicoDTO)
            throws IOException {
        var servico = this.commandProcessor.process(new DisponibilizarServicoCommand(servicoDTO));
        return ResponseEntity.ok()
                .body(this.objectMapper.createObjectNode().put("servicoId",
                        servico.getAggregateId().toString()));
    }

	@PutMapping("/{id}/ajustar")
	public ResponseEntity<JsonNode> ajustar(@PathVariable String id, @RequestBody ServicoDTO servicoDTO)
			throws IOException, ControlledException, IncompleteRegisterException {
		log.info(" >>> ajustar atendimentoDTO: {}", servicoDTO.toString());
		this.commandProcessor.process(new AjustarServicoCommand(UUID.fromString(id), servicoDTO));
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/cancelar")
	public ResponseEntity<JsonNode> cancelar(@PathVariable String id, @RequestBody ServicoDTO servicoDTO)
			throws IOException, ControlledException, IncompleteRegisterException {
		log.info(" >>> cancelar atendimentoDTO: {}", servicoDTO.toString());
		this.commandProcessor.process(new CancelarServicoCommand(UUID.fromString(id)));
		return ResponseEntity.ok().build();
	}
	
    @GetMapping("/{id}")
    public ResponseEntity<JpaServicoProjection> getServicoById(@PathVariable String id) {
        return this.jpaServicoProjectionRepository.findById(UUID.fromString(id)).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<JpaServicoProjection>> getServicoByNome(@PathVariable String nome) {
        List<JpaServicoProjection> jpaServicoProjections = this.jpaServicoProjectionRepository.findByNomeContainingIgnoreCase(nome);
        if (jpaServicoProjections == null) { 
        	return ResponseEntity.ok().build();
        } else {
        	return ResponseEntity.ok(jpaServicoProjections);
        }
    }
}
