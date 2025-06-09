package br.com.comigo.common.model.records._assistencia;

import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.common.model.records.TipoDeDocFiscal;

public record ClienteDTO(
		Long id,
		String nome,
		TipoDeDocFiscal tipoDeDocFiscal,
		String docFiscal,
		Telefone telefone,
		Telefone whatsapp) {

}
