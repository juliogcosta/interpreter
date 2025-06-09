package br.com.comigo.assistencia.domain.aggregate;

import java.util.EnumSet;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;

import br.com.comigo.assistencia.domain.aggregate.command.AjustarServicoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.CancelarServicoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.DisponibilizarServicoCommand;
import br.com.comigo.assistencia.domain.aggregate.command.IndisponibilizarServicoCommand;
import br.com.comigo.assistencia.domain.aggregate.entity.Servico;
import br.com.comigo.assistencia.domain.aggregate.event.ServicoAjustadoEvent;
import br.com.comigo.assistencia.domain.aggregate.event.ServicoCanceladoEvent;
import br.com.comigo.assistencia.domain.aggregate.event.ServicoDisponibilizadoEvent;
import br.com.comigo.assistencia.domain.aggregate.event.ServicoIndisponibilizadoEvent;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.error.AggregateStateException;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString(callSuper = true)
public class ServicoAggregate extends Aggregate {

    private Servico servico = new Servico();

    @JsonCreator
    public ServicoAggregate(@NonNull UUID aggregateId, int version) {
        super(aggregateId, version);
    }

    public void process(DisponibilizarServicoCommand command) {
    	log.info(" > trying process command {}", command.getAggregateType());
    	
        if (this.servico.getStatus() == null) {
        	log.info(" > version: {}", super.version);
        } else
            throw new AggregateStateException("Can't adjust status %s", this.servico.getStatus());
        
        log.info("\n > servico: {}\n", servico);
        
        super.applyChange(ServicoDisponibilizadoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .nome(command.getNome())
                .descricao(command.getDescricao())
                .certificacaoIso(command.getCertificacaoIso())
                .build());
    }

    public void process(AjustarServicoCommand command) {
        if (EnumSet.of(Servico.Status.DISPONIBILIZADO, Servico.Status.AJUSTADO)
                .contains(this.servico.getStatus())) {

        } else
            throw new AggregateStateException("Can't adjust status %s", this.servico.getStatus());
        super.applyChange(ServicoAjustadoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .descricao(command.getDescricao())
                .certificacaoIso(command.getCertificacaoIso())
                .build());
    }

    public void process(IndisponibilizarServicoCommand command) {
        if (EnumSet.of(Servico.Status.DISPONIBILIZADO, Servico.Status.AJUSTADO)
                .contains(this.servico.getStatus())) {

        } else
            throw new AggregateStateException("Can't adjust status %s", this.servico.getStatus());
        super.applyChange(ServicoIndisponibilizadoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .build());
    }

    public void process(CancelarServicoCommand command) {
        if (Servico.Status.INDISPONIBILIZADO == this.servico.getStatus()) {

        } else
            throw new AggregateStateException("Can't adjust status %s", this.servico.getStatus());
        super.applyChange(ServicoCanceladoEvent.builder()
                .aggregateId(super.aggregateId)
                .version(this.getNextVersion())
                .build());
    }

    public void apply(ServicoDisponibilizadoEvent event) {
        this.servico.setId(event.getAggregateId());
        this.servico.setStatus(Servico.Status.DISPONIBILIZADO);
        this.servico.setNome(event.getNome());
        this.servico.setDescricao(event.getDescricao());
        this.servico.setCertificacaoIso(event.getCertificacaoIso());
        this.servico.setDataHoraDisponibilizado(event.getCreatedDate());
        this.version = event.getVersion();
        
        log.info("\n > servico: {}\n", servico);
    }

    public void apply(ServicoAjustadoEvent event) {
        this.servico.setId(event.getAggregateId());
        this.servico.setStatus(Servico.Status.AJUSTADO);
        this.servico.setDescricao(event.getDescricao());
        this.servico.setCertificacaoIso(event.getCertificacaoIso());
        this.servico.setDataHoraAjustado(event.getCreatedDate());
        this.version = event.getVersion();
    }

    public void apply(ServicoIndisponibilizadoEvent event) {
        this.servico.setId(event.getAggregateId());
        this.servico.setStatus(Servico.Status.INDISPONIBILIZADO);
        this.servico.setDataHoraIndisponibilizado(event.getCreatedDate());
        this.version = event.getVersion();
    }

    public void apply(ServicoCanceladoEvent event) {
        this.servico.setId(event.getAggregateId());
        this.servico.setStatus(Servico.Status.CANCELADO);
        this.servico.setDataHoraCancelado(event.getCreatedDate());
        this.version = event.getVersion();
    }

    @NonNull
    @Override
    public String getAggregateType() {
        return AggregateType.SERVICO.toString();
    }
}
