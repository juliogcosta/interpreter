package br.com.comigo.assistencia.domain.aggregate.prestador;

import java.util.List;

import com.yc.core.common.model.records.Cnpj;
import com.yc.core.common.model.records.Email;
import com.yc.core.common.model.records.Endereco;
import com.yc.core.common.model.records.Telefone;

public class Prestador {

	public enum Status {
		ATIVO, INATIVO
	}

	private Long id;
	private String nome;
	private Cnpj cnpj;
	private Telefone telefone;
	private Telefone whatsapp;
	private Email email;
	private Endereco endereco;
	private Status status;
	private Boolean temCertificacaoIso;
	private List<ServicoDePrestador> servicoDePrestadors;
	
	public Prestador() {
		
	}

	public Prestador(Long id, String nome, Status status, Cnpj cnpj, Telefone telefone, Telefone whatsapp, Email email,
			Endereco endereco, Boolean temCertificadoIso) {
		this.id = id;
		this.nome = nome;
		this.cnpj = cnpj;
		this.telefone = telefone;
		this.whatsapp = whatsapp;
		this.email = email;
		this.endereco = endereco;
		this.status = status;
		this.temCertificacaoIso = temCertificadoIso;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cnpj getCnpj() {
		return this.cnpj;
	}

	public void setCnpj(Cnpj cnpj) {
		this.cnpj = cnpj;
	}

	public Telefone getTelefone() {
		return this.telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Telefone getWhatsapp() {
		return this.whatsapp;
	}

	public void setWhatsapp(Telefone whatsapp) {
		this.whatsapp = whatsapp;
	}

	public Email getEmail() {
		return this.email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public Endereco getEndereco() {
		return this.endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setTemCertificacaoIso(Boolean temCertificacaoIso) {
		this.temCertificacaoIso = temCertificacaoIso;
	}

	public Boolean getTemCertificacaoIso() {
		return temCertificacaoIso;
	}

	public List<ServicoDePrestador> getServicoDePrestadors() {
		return this.servicoDePrestadors;
	}

	public void setServicoDePrestadors(List<ServicoDePrestador> servicos) {
		this.servicoDePrestadors = servicos;
	}

	@Override
	public String toString() {
		return "Prestador [getId()=" + getId() + ", getNome()=" + getNome() + ", getCnpj()=" + getCnpj()
				+ ", getTelefone()=" + getTelefone() + ", getWhatsapp()=" + getWhatsapp() + ", getEmail()=" + getEmail()
				+ ", getEndereco()=" + getEndereco() + ", getStatus()=" + getStatus() + ", getTemCertificacaoIso()="
				+ getTemCertificacaoIso() + ", getServicoDePrestadors()=" + getServicoDePrestadors() + "]";
	}

}