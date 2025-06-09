package br.com.comigo.assistencia.adapter.outbount.restclient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.common.model.records.Telefone.Tipo;
import br.com.comigo.common.model.records._assistencia.ClienteDTO;
import br.com.comigo.common.model.records._assistencia.ServicoDePrestadorDTO;
import br.com.comigo.common.model.records._assistencia.ServicoDePrestadorDTO.PrestadorDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class AssistenciaService {

	private final AssistenciaRestClient assistenciaRestClient;
	
	public ClienteDTO findClienteById(Long id) throws Exception {
		return this.assistenciaRestClient.findClienteById(id);
	}
	
	public ServicoDePrestadorDTO findServicoDePrestadorById(UUID servicoId) throws Exception {
		ResponseEntity<String> responseEntity = this.assistenciaRestClient.findServicoDePrestadorById(servicoId.toString());
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			
		} else {
			throw new RuntimeException("Não foi possível recuperar o serviço prestado da carteira de serviços do prestador.");
		}
		log.info(" > responseEntity.body: {}", responseEntity.getBody());
		JSONObject jsObject = new JSONObject(responseEntity.getBody());
		Cnpj cnpj = new Cnpj(jsObject.getJSONObject("prestador").getJSONObject("cnpj").getString("cnpj"));
		Telefone telefone = new Telefone(jsObject.getJSONObject("prestador").getJSONObject("telefone").getString("numero"), 
				Tipo.valueOf(jsObject.getJSONObject("prestador").getJSONObject("telefone").getString("tipo")));
		PrestadorDTO prestadorDTO = new PrestadorDTO(jsObject.getJSONObject("prestador").getLong("id"), jsObject.getJSONObject("prestador").getString("nome"), cnpj, telefone);
		return new ServicoDePrestadorDTO(servicoId, jsObject.getString("nome"), prestadorDTO);
	}
	
	public List<ServicoDePrestadorDTO> findServicoDePrestadorsByIds(List<UUID> servicoIds) throws Exception {
		ResponseEntity<String> responseEntity = this.assistenciaRestClient.findServicoDePrestadorById(servicoIds.stream().map(id -> id.toString()).toString());
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			
		} else {
			throw new RuntimeException("Não foi possível recuperar o serviço prestado da carteira de serviços do prestador.");
		}
		JSONArray jsArray = new JSONArray(responseEntity.getBody());
		
		List<ServicoDePrestadorDTO> servicoDePrestadorDTOs = new ArrayList<>();
		for (int i = 0; i < jsArray.length(); i++) {
			JSONObject jsObject = jsArray.getJSONObject(i);

			Cnpj cnpj = new Cnpj(jsObject.getJSONObject("prestador").getJSONObject("cnpj").getString("cnpj"));
			Telefone telefone = new Telefone(jsObject.getJSONObject("prestador").getJSONObject("telefone").getString("numero"), 
					Tipo.valueOf(jsObject.getJSONObject("prestador").getJSONObject("telefone").getString("tipo")));
			PrestadorDTO prestadorDTO = new PrestadorDTO(jsObject.getJSONObject("prestador").getLong("id"), jsObject.getJSONObject("prestador").getString("nome"), cnpj, telefone);
			servicoDePrestadorDTOs.add(new ServicoDePrestadorDTO(UUID.fromString(jsObject.getString("id")), jsObject.getString("nome"), prestadorDTO));
		}
		return servicoDePrestadorDTOs;
	}
}
