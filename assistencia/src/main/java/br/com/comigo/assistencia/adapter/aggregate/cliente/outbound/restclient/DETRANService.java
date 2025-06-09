package br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.restclient;

import org.springframework.stereotype.Service;

import br.com.comigo.common.infrastructure.exception.ControlledException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DETRANService {
	//private DETRANRestClient detranRestClient;

	public Boolean consultarSituacaoDeVeiculo(String placa) throws ControlledException {
		try {
			/*
			ResponseEntity<String> responseEntity = this.detranRestClient.consultarSituacaoDeVeiculo(placa);
			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				JSONObject jsResponse = new JSONObject(responseEntity.getBody());
				return jsResponse.getBoolean("status");
			} else return false;
			 */
			return false;
		} catch (Exception e) {
			throw new ControlledException(e.getMessage());
		}
	}
}
