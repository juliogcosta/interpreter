package br.com.comigo.assistencia.adapter.inbound.aggregate.dto;

import java.sql.Timestamp;
import java.util.UUID;

import br.com.comigo.assistencia.domain.aggregate.entity.Servico;

public record ServicoDTO(
		UUID id, 
		String nome, 
		String descricao, 
		Servico.Status status, 
		Boolean certificacaoIso,
		Timestamp dataHoraDisponibilizado, 
		Timestamp dataHoraIndisponibilizado, 
		Timestamp dataHoraAjustado,
		Timestamp dataHoraCancelado) {
}