package br.com.comigo.assistencia.adapter.inbound.aggregate.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.comigo.assistencia.adapter.outbount.projection.respository.JpaServicoProjectionRepository;
import br.com.comigo.common.model.records._assistencia.ServicoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/unsec/servico")
@RequiredArgsConstructor
public class UnsecServicoController {

    private final JpaServicoProjectionRepository jpaServicoProjectionRepository;

    @GetMapping("/com-certificadoIso")
    public List<ServicoDTO> getServicosComCertificado() {
        return this.jpaServicoProjectionRepository.findByCertificacaoIso(true).stream()
        		.map(jpaServicoProjection -> {
        			return new ServicoDTO(jpaServicoProjection.getId(), jpaServicoProjection.getNome(), new ArrayList<>());
        		}).toList();
    }
}
