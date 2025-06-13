package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.restclient;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yc.core.common.infrastructure.exception.ControlledException;
import com.yc.core.common.model.records._assistencia.ServicoDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AssistenciaCatalogoService {
	
	private final AssistenciaCatalogoRestClient assistenciaCatalogoRestClient;
	
	public List<ServicoDTO> findAllServicosByCertificateIso() throws ControlledException {
		
		try {
			return this.assistenciaCatalogoRestClient.findAllServicosByCertificateISO();
		} catch (Exception e) {
			throw new ControlledException(e.getMessage());
		}
	}
	
	public ServicoDTO findServicoById(String uuid) throws ControlledException {
		
		try {
			return this.assistenciaCatalogoRestClient.findServicoByUuid(uuid);
		} catch (Exception e) {
			throw new ControlledException(e.getMessage());
		}
	}
}
