package br.com.comigo.assistencia.domain.aggregate.command;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.AtendimentoDTO;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.common.model.records.TipoDeDocFiscal;
import br.com.comigo.core.domain.command.Command;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString(callSuper = true)
public final class SolicitarAtendimentoCommand extends Command {

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

    public SolicitarAtendimentoCommand(AtendimentoDTO atendimentoDTO) {
        super(AggregateType.ATENDIMENTO.toString(), generateAggregateId());
        this.clienteId = atendimentoDTO.clienteId();
        this.clienteTipoDeDocFiscal = atendimentoDTO.clienteTipoDeDocFiscal();
        this.clienteDocFiscal = atendimentoDTO.clienteDocFiscal();
        this.clienteNome = atendimentoDTO.clienteNome();
        this.clienteTelefone = atendimentoDTO.clienteTelefone();
        this.clienteWhatsapp = atendimentoDTO.clienteWhatsapp();
        this.veiculoPlaca = atendimentoDTO.veiculoPlaca();
        this.tipoOcorrencia = atendimentoDTO.tipoOcorrencia();
        this.servicoId = atendimentoDTO.servicoId();
        this.servicoNome = atendimentoDTO.servicoNome();
        this.base = atendimentoDTO.base();
        this.origem = atendimentoDTO.origem();
    }

    private static UUID generateAggregateId() {
        return UUID.randomUUID();
    }
}
