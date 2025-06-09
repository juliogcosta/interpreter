package br.com.comigo.assistencia.domain.aggregate.event;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.common.model.records.TipoDeDocFiscal;
import br.com.comigo.core.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public final class AtendimentoSolicitadoEvent extends Event {

    private final String clienteId;
    private final TipoDeDocFiscal clienteTipoDeDocFiscal;
    private final String clienteDocFiscal;
    private final String clienteNome;
    private final Telefone clienteTelefone;
    private final Telefone clienteWhatsapp;
    private final String veiculoPlaca;
    private final String tipoOcorrencia;
    private final String servicoId;
    private final String servicoNome;
    private final Endereco base;
    private final Endereco origem;

    @JsonCreator
    @Builder
    public AtendimentoSolicitadoEvent(UUID aggregateId, 
    		int version, 
    		String clienteId,
    		TipoDeDocFiscal clienteTipoDeDocFiscal,
    		String clienteDocFiscal,
    		String clienteNome,
    		Telefone clienteTelefone,
    		Telefone clienteWhatsapp,
    		String veiculoPlaca,
    		String tipoOcorrencia,
    		String servicoId, 
    		String servicoNome,
    		Endereco base,
    		Endereco origem) {
        super(aggregateId, version);

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
        this.base = base;
        this.origem = origem;
    }

    @Nonnull
    @Override
    public String getEventType() {
        return EventType.ATENDIMENTO_SOLICITADO.toString();
    }
}
