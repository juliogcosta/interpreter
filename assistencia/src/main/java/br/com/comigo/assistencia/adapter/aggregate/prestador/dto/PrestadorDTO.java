package br.com.comigo.assistencia.adapter.aggregate.prestador.dto;

import com.yc.core.common.model.records.Cnpj;
import com.yc.core.common.model.records.Email;
import com.yc.core.common.model.records.Endereco;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador.Status;

public record PrestadorDTO(
		Long id, 
		String nome, 
		Cnpj cnpj, 
		Telefone telefone, 
		Telefone whatsapp, 
		Email email,
		Endereco endereco, 
		Status status, 
		Boolean temCertificacaoIso) {
}