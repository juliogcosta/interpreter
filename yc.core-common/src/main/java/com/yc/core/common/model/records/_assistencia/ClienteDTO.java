package com.yc.core.common.model.records._assistencia;

import com.yc.core.common.model.records.Telefone;
import com.yc.core.common.model.records.TipoDeDocFiscal;

public record ClienteDTO(
		Long id,
		String nome,
		TipoDeDocFiscal tipoDeDocFiscal,
		String docFiscal,
		Telefone telefone,
		Telefone whatsapp) {

}
