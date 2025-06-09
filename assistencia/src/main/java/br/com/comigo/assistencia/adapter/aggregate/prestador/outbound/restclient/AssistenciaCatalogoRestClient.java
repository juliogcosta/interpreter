package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.restclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.comigo.common.model.records._assistencia.ServicoDTO;

@FeignClient(name = "${app.assistencia-catalogo.name}", url = "${app.assistencia-catalogo.address}:${app.assistencia-catalogo.port}")
public interface AssistenciaCatalogoRestClient {

    @GetMapping("/unsec/servico/com-certificadoIso")	
	public List<ServicoDTO> findAllServicosByCertificateISO() throws Exception;

    @GetMapping("/servico/{uuid}")	
	public ServicoDTO findServicoByUuid(@PathVariable String uuid) throws Exception;
}
