package br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.restclient;

import org.springframework.http.ResponseEntity;

public interface DETRANRestClient {

	public ResponseEntity<String> consultarSituacaoDeVeiculo(String placa);
	
	public record VeiculoDTO(Boolean situacao) {};
}
