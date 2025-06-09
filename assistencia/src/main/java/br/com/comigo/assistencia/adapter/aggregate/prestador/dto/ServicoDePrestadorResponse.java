package br.com.comigo.assistencia.adapter.aggregate.prestador.dto;

import java.util.List;
import java.util.UUID;

import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;

public record ServicoDePrestadorResponse(
		UUID id, 
		String nome, 
		Status status,
		PrestadorResponse prestador, 
		List<ItemDeServicoDePrestadorResponse> itemDeServicoDePrestadorDTOs) {
	public record ItemDeServicoDePrestadorResponse (
			String nome,
			String unidadeDeMedida,
			Integer precoUnitario) { }
}