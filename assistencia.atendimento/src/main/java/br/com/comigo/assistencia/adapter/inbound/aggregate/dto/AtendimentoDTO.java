package br.com.comigo.assistencia.adapter.inbound.aggregate.dto;

import br.com.comigo.assistencia.domain.aggregate.entity.Atendimento;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.common.model.records.TipoDeDocFiscal;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public record AtendimentoDTO(
	UUID id,
    String prestadorId,
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
    Atendimento.Status status,
    Timestamp dataHoraSolicitado,
    Timestamp dataHoraConfirmado,
    Timestamp dataHoraAjustado,
    Timestamp dataHoraFinalizado,
    Timestamp dataHoraCancelado,
    String descricao,
    Endereco origem,
    Endereco destino,
    Endereco base,
    List<ItemDTO> items) {
}