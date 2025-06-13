package com.yc.core.common.model.records._assistencia;

import java.util.UUID;

import com.yc.core.common.model.records.Cnpj;
import com.yc.core.common.model.records.Telefone;

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
