package br.com.comigo.assistencia.domain.aggregate.entity;

import java.sql.Timestamp;
import java.util.UUID;

public class Servico {

    public enum Status {
    	DISPONIBILIZADO, INDISPONIBILIZADO, AJUSTADO, CANCELADO
    }

    private UUID id = null;
    private Integer version = null;
    private String nome = null;
    private String descricao = null;
    private Status status = null;
    private Boolean certificacaoIso;
    private Timestamp dataHoraDisponibilizado = null;
    private Timestamp dataHoraIndisponibilizado = null;
    private Timestamp dataHoraAjustado = null;
    private Timestamp dataHoraCancelado = null;

    public Servico() {
    }

    public Servico(UUID id, String nome, String descricao,
            Status status, Boolean certificacaoIso, Timestamp dataHoraDisponibilizado, Timestamp dataHoraIndisponibilizado, 
            Timestamp dataHoraCancelado) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.certificacaoIso = certificacaoIso;
        this.dataHoraDisponibilizado = dataHoraDisponibilizado;
        this.dataHoraIndisponibilizado = dataHoraIndisponibilizado;
        this.dataHoraCancelado = dataHoraCancelado;
        this.descricao = descricao;
    }

    public UUID getId() {
        return this.id;
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Boolean getCertificacaoIso() {
		return certificacaoIso;
	}
	
	public void setCertificacaoIso(Boolean certificacaoIso) {
		this.certificacaoIso = certificacaoIso;
	}

	public Timestamp getDataHoraDisponibilizado() {
		return dataHoraDisponibilizado;
	}

	public void setDataHoraDisponibilizado(Timestamp dataHoraDisponibilizado) {
		this.dataHoraDisponibilizado = dataHoraDisponibilizado;
	}

	public Timestamp getDataHoraIndisponibilizado() {
		return dataHoraIndisponibilizado;
	}

	public void setDataHoraIndisponibilizado(Timestamp dataHoraIndisponibilizado) {
		this.dataHoraIndisponibilizado = dataHoraIndisponibilizado;
	}

	public Timestamp getDataHoraCancelado() {
		return dataHoraCancelado;
	}

	public void setDataHoraCancelado(Timestamp dataHoraCancelado) {
		this.dataHoraCancelado = dataHoraCancelado;
	}

	public Timestamp getDataHoraAjustado() {
		return dataHoraAjustado;
	}
	
	public void setDataHoraAjustado(Timestamp dataHoraAjustado) {
		this.dataHoraAjustado = dataHoraAjustado;
	}
	
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Servico [id=" + id + ", version=" + version + ", nome=" + nome + ", descricao=" + descricao
				+ ", status=" + status + ", dataHoraDisponibilizado=" + dataHoraDisponibilizado
				+ ", dataHoraIndisponibilizado=" + dataHoraIndisponibilizado + ", dataHoraCancelado="
				+ dataHoraCancelado + "]";
	}

}
