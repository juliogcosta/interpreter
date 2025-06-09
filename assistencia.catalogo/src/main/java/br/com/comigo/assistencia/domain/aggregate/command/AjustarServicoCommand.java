package br.com.comigo.assistencia.domain.aggregate.command;

import java.util.UUID;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.ServicoDTO;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public final class AjustarServicoCommand extends Command {

    private final String descricao;
    private final Boolean certificacaoIso;

    public AjustarServicoCommand(UUID aggregateId, ServicoDTO servicoDTO) {
        super(AggregateType.SERVICO.toString(), aggregateId);

        this.descricao = servicoDTO.descricao();
        this.certificacaoIso = servicoDTO.certificacaoIso();
    }
}
