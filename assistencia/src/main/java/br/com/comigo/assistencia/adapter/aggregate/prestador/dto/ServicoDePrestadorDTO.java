package br.com.comigo.assistencia.adapter.aggregate.prestador.dto;

import java.util.List;
import java.util.UUID;

import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;

public record ServicoDePrestadorDTO(
		UUID id, 
		Status status,
		List<ItemDeServicoDePrestadorDTO> itemDeServicoDePrestadors) {
	public record ItemDeServicoDePrestadorDTO (
			String nome,
			String unidadeDeMedida,
			Integer precoUnitario) {}
}