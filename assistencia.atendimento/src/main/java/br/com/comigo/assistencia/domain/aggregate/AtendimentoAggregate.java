package br.com.comigo.assistencia.domain.aggregate;

import java.util.EnumSet;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.comigo.assistencia.domain.aggregate.command.AjustarAtendimentoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.CancelarAtendimentoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.ConfirmarAtendimentoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.FinalizarAtendimentoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.SolicitarAtendimentoCommand;
import br.com.comigo.assistencia.domain.aggregate.entity.Atendimento;
import br.com.comigo.assistencia.domain.aggregate.event.AtendimentoAjustadoEvent;
import br.com.comigo.assistencia.domain.aggregate.event.AtendimentoCanceladoEvent;
import br.com.comigo.assistencia.domain.aggregate.event.AtendimentoConfirmadoEvent;
import br.com.comigo.assistencia.domain.aggregate.event.AtendimentoFinalizadoEvent;
import br.com.comigo.assistencia.domain.aggregate.event.AtendimentoSolicitadoEvent;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.error.AggregateStateException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString(callSuper = true)
public class AtendimentoAggregate extends Aggregate {

    private Atendimento atendimento = new Atendimento();

    @JsonCreator
    public AtendimentoAggregate(@NonNull UUID aggregateId, int version) {
        super(aggregateId, version);
    }

    /*
     * > SOLICITADO
     * SOLICITADO > CONFIRMADO > FINALIZADO
     * SOLICITADO > CONFIRMADO > CANCELADO
     * SOLICITADO > CANCELADO
     */

    public void process(SolicitarAtendimentoCommand command) {
    	log.info(" > trying process command {}", command.getAggregateType());
    	
        if (this.atendimento.getStatus() == null) {
        	log.info(" > version: {}", super.version);
        } else
            throw new AggregateStateException("O status %s não pode ser ajustado", this.atendimento.getStatus());
        super.applyChange(AtendimentoSolicitadoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .clienteId(command.getClienteId())
                .clienteTipoDeDocFiscal(command.getClienteTipoDeDocFiscal())
                .clienteDocFiscal(command.getClienteDocFiscal())
                .clienteNome(command.getClienteNome())
                .clienteTelefone(command.getClienteTelefone())
                .clienteWhatsapp(command.getClienteWhatsapp())
                .veiculoPlaca(command.getVeiculoPlaca())
                .tipoOcorrencia(command.getTipoOcorrencia())
                .servicoId(command.getServicoId())
                .servicoNome(command.getServicoNome())
                .base(command.getBase())
                .origem(command.getOrigem())
                .build());
    }

    public void process(AjustarAtendimentoCommand command) {
        if (EnumSet.of(Atendimento.Status.SOLICITADO, Atendimento.Status.AJUSTADO)
                .contains(this.atendimento.getStatus())) {

        } else
            throw new AggregateStateException("O status %s não pode ser ajustado", this.atendimento.getStatus());
        super.applyChange(AtendimentoAjustadoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .servicoId(command.getServicoId())
                .servicoNome(command.getServicoNome())
                .origem(command.getOrigem())
                .destino(command.getDestino())
                .descricao(command.getDescricao())
                .items(command.getItems())
                .build());
    }

    public void process(ConfirmarAtendimentoCommand command) {
    	if (EnumSet.of(Atendimento.Status.SOLICITADO, Atendimento.Status.CONFIRMADO, Atendimento.Status.FINALIZADO, Atendimento.Status.CANCELADO)
                .contains(this.atendimento.getStatus())) {
            throw new AggregateStateException("O status %s não pode ser ajustado", this.atendimento.getStatus());
        }
        super.applyChange(AtendimentoConfirmadoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .prestadorId(command.getPrestadorId())
                .build());
    }

    public void process(FinalizarAtendimentoCommand command) {
        if (this.atendimento.getStatus() == Atendimento.Status.CONFIRMADO) {

        } else
            throw new AggregateStateException("O status %s não pode ser ajustado", this.atendimento.getStatus());
        super.applyChange(AtendimentoFinalizadoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .build());
    }

    public void process(CancelarAtendimentoCommand command) {
        if (EnumSet.of(Atendimento.Status.SOLICITADO, Atendimento.Status.AJUSTADO, Atendimento.Status.CONFIRMADO)
                .contains(this.atendimento.getStatus())) {

        } else
            throw new AggregateStateException("O status %s não pode ser ajustado", this.atendimento.getStatus());
        super.applyChange(AtendimentoCanceladoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .build());
    }

    public void apply(AtendimentoSolicitadoEvent event) {
        this.atendimento.setId(event.getAggregateId());
        this.atendimento.setStatus(Atendimento.Status.SOLICITADO);
        this.atendimento.setClienteId(event.getClienteId() == null? null : Long.valueOf(event.getClienteId()));
        this.atendimento.setClienteTipoDeDocFiscal(event.getClienteTipoDeDocFiscal());
        this.atendimento.setClienteDocFiscal(event.getClienteDocFiscal());
        this.atendimento.setClienteNome(event.getClienteNome());
        this.atendimento.setClienteTelefone(event.getClienteTelefone());
        this.atendimento.setClienteWhatsapp(event.getClienteWhatsapp());
        this.atendimento.setVeiculoPlaca(event.getVeiculoPlaca());
        this.atendimento.setTipoOcorrencia(event.getTipoOcorrencia());
        this.atendimento.setServicoId(UUID.fromString(event.getServicoId()));
        this.atendimento.setServicoNome(event.getServicoNome());
        this.atendimento.setDataHoraSolicitado(event.getCreatedDate());
        this.atendimento.setBase(event.getBase());
        this.atendimento.setOrigem(event.getOrigem());
        this.version = event.getVersion();
    }

    public void apply(AtendimentoConfirmadoEvent event) {
        this.atendimento.setId(event.getAggregateId());
        this.atendimento.setStatus(Atendimento.Status.CONFIRMADO);
        this.atendimento.setPrestadorId(Long.valueOf(event.getPrestadorId()));
        this.atendimento.setDataHoraConfirmado(event.getCreatedDate());
        this.version = event.getVersion();
    }

    public void apply(AtendimentoAjustadoEvent event) {
        this.atendimento.setId(event.getAggregateId());
        this.atendimento.setStatus(Atendimento.Status.AJUSTADO);
        this.atendimento.setServicoId(UUID.fromString(event.getServicoId()));
        this.atendimento.setServicoNome(event.getServicoNome());
        this.atendimento.setOrigem(event.getOrigem());
        this.atendimento.setDestino(event.getDestino());
        this.atendimento.setDescricao(event.getDescricao());
        this.atendimento.setDataHoraAjustado(event.getCreatedDate());
        this.atendimento.setItems(event.getItems());
        this.version = event.getVersion();
    }

    public void apply(AtendimentoFinalizadoEvent event) {
        this.atendimento.setId(event.getAggregateId());
        this.atendimento.setStatus(Atendimento.Status.FINALIZADO);
        this.atendimento.setDataHoraFinalizado(event.getCreatedDate());
        this.version = event.getVersion();
    }

    public void apply(AtendimentoCanceladoEvent event) {
        this.atendimento.setId(event.getAggregateId());
        this.atendimento.setStatus(Atendimento.Status.CANCELADO);
        this.atendimento.setDataHoraCancelado(event.getCreatedDate());
        this.version = event.getVersion();
    }

    @NonNull
    @Override
    public String getAggregateType() {
        return AggregateType.ATENDIMENTO.toString();
    }
}
