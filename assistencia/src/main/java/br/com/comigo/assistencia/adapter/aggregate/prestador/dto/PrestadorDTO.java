package br.com.comigo.assistencia.adapter.aggregate.prestador.dto;

import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador.Status;
import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Email;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;

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