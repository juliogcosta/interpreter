package br.com.comigo.assistencia.domain.aggregate.prestador;

import java.util.List;
import java.util.UUID;

public class ServicoDePrestador {

	public enum Status {
		ATIVO, INATIVO,
	}

	private UUID id = null;
	private String nome = null;
	private Status status = null;
	private Prestador prestador = null;
	private List<ItemDeServicoDePrestador> itemDeServicoDePrestadors = null;

	public ServicoDePrestador(UUID uuid, String nome, Status status, 
			Prestador prestador, List<ItemDeServicoDePrestador> itemDeServicoDePrestadors) {
		this.id = uuid;
		this.nome = nome;
		this.status = status;
		this.prestador = prestador;
		this.itemDeServicoDePrestadors = itemDeServicoDePrestadors;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Prestador getPrestador() {
		return prestador;
	}

	public void setPrestador(Prestador prestador) {
		this.prestador = prestador;
	}
	
	public List<ItemDeServicoDePrestador> getItemDeServicoDePrestadors() {
		return itemDeServicoDePrestadors;
	}
	
	public void setItemDeServicoDePrestadors(List<ItemDeServicoDePrestador> items) {
		this.itemDeServicoDePrestadors = items;
	}

	@Override
	public String toString() {
		return "Servico [getUuid()=" + getId() + ", getNome()=" + getNome() + ", getStatus()=" + getStatus()
				+ ", getPrestador()=" + getPrestador() + ", getItemDeServicoDePrestadors()=" + getItemDeServicoDePrestadors() + "]";
	}

}