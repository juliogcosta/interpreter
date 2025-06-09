package br.com.comigo.assistencia.domain.aggregate.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.common.model.records.TipoDeDocFiscal;

public class Atendimento {

    public enum Status {
        SOLICITADO, CONFIRMADO, AJUSTADO, FINALIZADO, CANCELADO
    }

    private UUID id = null;
    private Long prestadorId = null;
    private Long clienteId = null;
	private TipoDeDocFiscal clienteTipoDeDocFiscal;
    private String clienteDocFiscal;
    private String clienteNome = null;
    private Telefone clienteTelefone = null;
    private Telefone clienteWhatsapp = null;
    private String veiculoPlaca = null;
    private String tipoOcorrencia = null;
    private UUID servicoId = null;
    private String servicoNome = null;
    private Status status = null;
    private Timestamp dataHoraSolicitado = null;
    private Timestamp dataHoraConfirmado = null;
    private Timestamp dataHoraAjustado = null;
    private Timestamp dataHoraFinalizado = null;
    private Timestamp dataHoraCancelado = null;
    private String descricao = null;
    private Endereco base = null;
    private Endereco origem = null;
    private Endereco destino = null;
    private List<Item> items = null;

    public Atendimento() {
    }

    public Atendimento(UUID id, Long prestadorId, Long clienteId, TipoDeDocFiscal clienteTipoDeDocFiscal, String clienteNome, 
    		String clienteDocFiscal, Telefone clienteTelefone, Telefone clienteWhatsapp, String veiculoPlaca, String tipoOcorrencia, 
    		UUID servicoId, String servicoNome, Status status, Timestamp dataHoraChamado, Timestamp dataHoraConfirmado, Timestamp dataHoraExecutando,
            Timestamp dataHoraFinalizado, Timestamp dataHoraCancelado, String descricao, Endereco origem,
            Endereco destino, Endereco base) {
        this.id = id;
        this.prestadorId = prestadorId;
        this.clienteId = clienteId;
        this.clienteTipoDeDocFiscal = clienteTipoDeDocFiscal;
        this.clienteDocFiscal = clienteDocFiscal;
        this.clienteNome = clienteNome;
        this.clienteTelefone = clienteTelefone;
        this.clienteWhatsapp = clienteWhatsapp;
        this.veiculoPlaca = veiculoPlaca;
        this.tipoOcorrencia = tipoOcorrencia;
        this.servicoId = servicoId;
        this.servicoNome = servicoNome;
        this.status = status;
        this.dataHoraSolicitado = dataHoraChamado;
        this.dataHoraConfirmado = dataHoraConfirmado;
        this.dataHoraAjustado = dataHoraExecutando;
        this.dataHoraFinalizado = dataHoraFinalizado;
        this.dataHoraCancelado = dataHoraCancelado;
        this.descricao = descricao;
        this.origem = origem;
        this.destino = destino;
        this.base = base;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getPrestadorId() {
        return this.prestadorId;
    }

    public void setPrestadorId(Long prestadorId) {
        this.prestadorId = prestadorId;
    }

    public Long getClienteId() {
        return this.clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public TipoDeDocFiscal getClienteTipoDeDocFiscal() {
		return clienteTipoDeDocFiscal;
	}
    
    public void setClienteTipoDeDocFiscal(TipoDeDocFiscal clienteTipoDeDocFiscal) {
		this.clienteTipoDeDocFiscal = clienteTipoDeDocFiscal;
	}
    
    public String getClienteDocFiscal() {
		return clienteDocFiscal;
	}
    
    public void setClienteDocFiscal(String clienteDocFiscal) {
		this.clienteDocFiscal = clienteDocFiscal;
	}

    public String getClienteNome() {
        return this.clienteNome;
    }

    public void setClienteNome(String clienteNome) {
        this.clienteNome = clienteNome;
    }

    public Telefone getClienteTelefone() {
        return this.clienteTelefone;
    }

    public void setClienteTelefone(Telefone clienteTelefone) {
        this.clienteTelefone = clienteTelefone;
    }

    public Telefone getClienteWhatsapp() {
        return this.clienteWhatsapp;
    }

    public void setClienteWhatsapp(Telefone clienteWhatsapp) {
        this.clienteWhatsapp = clienteWhatsapp;
    }

    public String getVeiculoPlaca() {
        return this.veiculoPlaca;
    }

    public void setVeiculoPlaca(String veiculoPlaca) {
        this.veiculoPlaca = veiculoPlaca;
    }

    public String getTipoOcorrencia() {
        return this.tipoOcorrencia;
    }

    public void setTipoOcorrencia(String tipoOcorrencia) {
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public UUID getServicoId() {
        return this.servicoId;
    }

    public void setServicoId(UUID tipoServico) {
        this.servicoId = tipoServico;
    }
    
    public String getServicoNome() {
		return servicoNome;
	}
    
    public void setServicoNome(String servicoNome) {
		this.servicoNome = servicoNome;
	}

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getDataHoraSolicitado() {
        return this.dataHoraSolicitado;
    }

    public void setDataHoraSolicitado(Timestamp dataHoraSolicitado) {
        this.dataHoraSolicitado = dataHoraSolicitado;
    }

    public Timestamp getDataHoraConfirmado() {
        return this.dataHoraConfirmado;
    }

    public void setDataHoraConfirmado(Timestamp dataHoraConfirmado) {
        this.dataHoraConfirmado = dataHoraConfirmado;
    }

    public Timestamp getDataHoraAjustado() {
        return this.dataHoraAjustado;
    }

    public void setDataHoraAjustado(Timestamp dataHoraAjustado) {
        this.dataHoraAjustado = dataHoraAjustado;
    }

    public Timestamp getDataHoraFinalizado() {
        return this.dataHoraFinalizado;
    }

    public void setDataHoraFinalizado(Timestamp dataHoraFinalizado) {
        this.dataHoraFinalizado = dataHoraFinalizado;
    }

    public Timestamp getDataHoraCancelado() {
        return this.dataHoraCancelado;
    }

    public void setDataHoraCancelado(Timestamp dataHoraCancelado) {
        this.dataHoraCancelado = dataHoraCancelado;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Endereco getOrigem() {
        return this.origem;
    }

    public void setOrigem(Endereco origem) {
        this.origem = origem;
    }

    public Endereco getDestino() {
        return this.destino;
    }

    public void setDestino(Endereco destino) {
        this.destino = destino;
    }

    public Endereco getBase() {
        return this.base;
    }

    public void setBase(Endereco base) {
        this.base = base;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

	@Override
	public String toString() {
		return "Atendimento [id=" + id + ", prestadorId=" + prestadorId + ", clienteId=" + clienteId
				+ ", clienteTipoDeDocFiscal=" + clienteTipoDeDocFiscal + ", clienteDocFiscal=" + clienteDocFiscal
				+ ", clienteNome=" + clienteNome + ", clienteTelefone=" + clienteTelefone + ", clienteWhatsapp="
				+ clienteWhatsapp + ", veiculoPlaca=" + veiculoPlaca + ", tipoOcorrencia=" + tipoOcorrencia
				+ ", servicoId=" + servicoId + ", status=" + status + ", dataHoraSolicitado=" + dataHoraSolicitado
				+ ", dataHoraConfirmado=" + dataHoraConfirmado + ", dataHoraAjustado=" + dataHoraAjustado
				+ ", dataHoraFinalizado=" + dataHoraFinalizado + ", dataHoraCancelado=" + dataHoraCancelado
				+ ", descricao=" + descricao + ", base=" + base + ", origem=" + origem + ", destino=" + destino
				+ ", items=" + items + "]";
	}

}
