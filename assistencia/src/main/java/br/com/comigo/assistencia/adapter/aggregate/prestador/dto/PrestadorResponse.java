package br.com.comigo.assistencia.adapter.aggregate.prestador.dto;

import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador.Status;
import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Email;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PrestadorResponse {

	private Long id;
	private String nome;
	private Cnpj cnpj;
	private Telefone telefone;
	private Telefone whatsapp;
	private Email email;
	private Endereco endereco;
	private Status status;
	private Boolean temCertificacaoIso;
	
}