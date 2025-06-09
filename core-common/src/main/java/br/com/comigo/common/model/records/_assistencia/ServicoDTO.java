package br.com.comigo.common.model.records._assistencia;

import java.util.List;
import java.util.UUID;

public record ServicoDTO (
		UUID uuid, 
		String nome, 
		List<ItemDeServicoDTO> itemDeServicos) {
	public record ItemDeServicoDTO (
			String servicoNome, 
			String nome) { };
}
