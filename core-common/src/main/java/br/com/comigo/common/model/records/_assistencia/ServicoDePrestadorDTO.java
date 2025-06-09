package br.com.comigo.common.model.records._assistencia;

import java.util.UUID;

import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Telefone;

public record ServicoDePrestadorDTO(
		UUID id,
		String nome,
		PrestadorDTO prestador) {
	public record PrestadorDTO (
			Long id,
			String nome,
			Cnpj cnpj,
			Telefone telefone
			) { }
}
