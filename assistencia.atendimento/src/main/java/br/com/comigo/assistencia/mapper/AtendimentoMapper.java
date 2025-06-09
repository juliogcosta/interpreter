package br.com.comigo.assistencia.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.AtendimentoDTO;
import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.AtendimentoResponse;
import br.com.comigo.assistencia.adapter.inbound.aggregate.dto.ItemResponse;
import br.com.comigo.assistencia.adapter.outbount.projection.JpaAtendimentoProjection;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.common.model.records._assistencia.ClienteDTO;
import br.com.comigo.common.model.records._assistencia.ServicoDePrestadorDTO;

@Mapper(componentModel = "spring")
public interface AtendimentoMapper {
	default AtendimentoDTO adjust(AtendimentoDTO atendimentoDTO, ClienteDTO clienteDTO) {
		return new AtendimentoDTO(
				atendimentoDTO.id(), 
				atendimentoDTO.prestadorId(), 
				atendimentoDTO.clienteId(), 
				clienteDTO.tipoDeDocFiscal(), 
				clienteDTO.docFiscal(), 
				clienteDTO.nome(), 
				clienteDTO.telefone(), 
				clienteDTO.whatsapp(), 
				atendimentoDTO.veiculoPlaca(), 
				atendimentoDTO.tipoOcorrencia(), 
				atendimentoDTO.servicoId(), 
				atendimentoDTO.servicoNome(), 
				atendimentoDTO.status(), 
				atendimentoDTO.dataHoraSolicitado(), 
				atendimentoDTO.dataHoraConfirmado(), 
				atendimentoDTO.dataHoraAjustado(),
				atendimentoDTO.dataHoraFinalizado(), 
				atendimentoDTO.dataHoraCancelado(), 
				atendimentoDTO.descricao(), 
				atendimentoDTO.origem(), 
				atendimentoDTO.destino(), 
				atendimentoDTO.base(), 
				atendimentoDTO.items());
	}

	default AtendimentoResponse toResponse(JpaAtendimentoProjection jpaAtendimentoProjection, ServicoDePrestadorDTO servicoDePrestadorDTO) {
		
		boolean existeNaBase = false;
		
		Telefone telefone = new Telefone(jpaAtendimentoProjection.getClienteTelefone().getNumero(), 
				jpaAtendimentoProjection.getClienteTelefone().getTipo());

		
		Telefone whatsapp = null;
		if (jpaAtendimentoProjection.getClienteWhatsapp() == null) {
			
		} else {
			new Telefone(jpaAtendimentoProjection.getClienteWhatsapp().getNumero(), 
				jpaAtendimentoProjection.getClienteWhatsapp().getTipo());
		}
		
		Endereco origem = new Endereco(
				jpaAtendimentoProjection.getOrigem().getTipo(), 
				jpaAtendimentoProjection.getOrigem().getLogradouro(), 
				jpaAtendimentoProjection.getOrigem().getNumero(), 
				jpaAtendimentoProjection.getOrigem().getComplemento(), 
				jpaAtendimentoProjection.getOrigem().getBairro(), 
				jpaAtendimentoProjection.getOrigem().getCidade(), 
				jpaAtendimentoProjection.getOrigem().getEstado(), 
				jpaAtendimentoProjection.getOrigem().getCep());
		
		Endereco destino = null;
		if (jpaAtendimentoProjection.getDestino() == null) {
			
		} else {
			new Endereco(
					jpaAtendimentoProjection.getDestino().getTipo(), 
					jpaAtendimentoProjection.getDestino().getLogradouro(), 
					jpaAtendimentoProjection.getDestino().getNumero(), 
					jpaAtendimentoProjection.getDestino().getComplemento(), 
					jpaAtendimentoProjection.getDestino().getBairro(), 
					jpaAtendimentoProjection.getDestino().getCidade(), 
					jpaAtendimentoProjection.getDestino().getEstado(), 
					jpaAtendimentoProjection.getDestino().getCep());
		}
		
		Endereco base = new Endereco(
				jpaAtendimentoProjection.getBase().getTipo(), 
				jpaAtendimentoProjection.getBase().getLogradouro(), 
				jpaAtendimentoProjection.getBase().getNumero(), 
				jpaAtendimentoProjection.getBase().getComplemento(), 
				jpaAtendimentoProjection.getBase().getBairro(), 
				jpaAtendimentoProjection.getBase().getCidade(), 
				jpaAtendimentoProjection.getBase().getEstado(), 
				jpaAtendimentoProjection.getBase().getCep());

		List<ItemResponse> itemResponses = jpaAtendimentoProjection.getItems().stream()
				.map(item -> {
					return new ItemResponse(
							item.getNome(), 
							item.getUnidadeDeMedida(), 
							item.getPrecoUnitario(), 
							item.getQuantidade(), 
							item.getObservacao());
				}).toList();

		AtendimentoResponse atendimentoResponse = new AtendimentoResponse(
				jpaAtendimentoProjection.getId(), 
				jpaAtendimentoProjection.getPrestadorId(), 
				servicoDePrestadorDTO.prestador().nome(), 
				servicoDePrestadorDTO.prestador().cnpj(), 
				servicoDePrestadorDTO.prestador().telefone(), 
				jpaAtendimentoProjection.getClienteTipoDeDocFiscal(), 
				jpaAtendimentoProjection.getClienteDocFiscal(), 
				jpaAtendimentoProjection.getClienteNome(), 
				telefone, 
				whatsapp, 
				existeNaBase, 
				jpaAtendimentoProjection.getVeiculoPlaca(), 
				jpaAtendimentoProjection.getTipoOcorrencia(), 
				jpaAtendimentoProjection.getServicoId(), 
				servicoDePrestadorDTO.nome(), 
				jpaAtendimentoProjection.getStatus(), 
				jpaAtendimentoProjection.getDataHoraSolicitado(), 
				jpaAtendimentoProjection.getDataHoraConfirmado(), 
				jpaAtendimentoProjection.getDataHoraAjustado(), 
				jpaAtendimentoProjection.getDataHoraFinalizado(), 
				jpaAtendimentoProjection.getDataHoraCancelado(), 
				jpaAtendimentoProjection.getDescricao(), 
				origem,
				destino, 
				base, 
				itemResponses);
		return atendimentoResponse;
	}
}
