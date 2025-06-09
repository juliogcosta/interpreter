package br.com.comigo.assistencia.application.service.event;

import br.com.comigo.assistencia.adapter.outbount.projection.JpaAtendimentoProjection;
import br.com.comigo.assistencia.adapter.outbount.projection.JpaItemProjection;
import br.com.comigo.assistencia.adapter.outbount.projection.respository.JpaAtendimentoProjectionRepository;
import br.com.comigo.assistencia.adapter.outbount.util.JpaEndereco;
import br.com.comigo.assistencia.adapter.outbount.util.JpaTelefone;
import br.com.comigo.assistencia.domain.aggregate.AggregateType;
import br.com.comigo.assistencia.domain.aggregate.AtendimentoAggregate;
import br.com.comigo.core.application.service.event.handler.SyncEventHandler;
import br.com.comigo.core.domain.Aggregate;
import br.com.comigo.core.domain.event.Event;
import br.com.comigo.core.domain.event.EventWithId;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Component
@RequiredArgsConstructor
@Slf4j
public class AtendimentoProjectionUpdater implements SyncEventHandler {

	private final JpaAtendimentoProjectionRepository jpaOrderProjectionRepository;

	@Override
	public void handleEvents(List<EventWithId<Event>> events, Aggregate aggregate) {
		log.debug("Updating read model for {}", aggregate);
		this.updateAtendimentoProjection((AtendimentoAggregate) aggregate);
	}

	private void updateAtendimentoProjection(AtendimentoAggregate aggregate) {
		log.info(" > aggregate: {}", aggregate);
		JpaAtendimentoProjection jpaAtendimentoProjection = new JpaAtendimentoProjection();
		jpaAtendimentoProjection.setId(aggregate.getAggregateId());
		jpaAtendimentoProjection.setVersion(aggregate.getVersion());
		jpaAtendimentoProjection.setStatus(aggregate.getAtendimento().getStatus());

		if (aggregate.getAtendimento().getPrestadorId() == null) {

		} else {
			jpaAtendimentoProjection.setPrestadorId(aggregate.getAtendimento().getPrestadorId().toString());
		}

		if (aggregate.getAtendimento().getClienteId() == null) {
			jpaAtendimentoProjection.setClienteNome(aggregate.getAtendimento().getClienteNome());
			jpaAtendimentoProjection.setClienteTipoDeDocFiscal(aggregate.getAtendimento().getClienteTipoDeDocFiscal());
			jpaAtendimentoProjection.setClienteDocFiscal(aggregate.getAtendimento().getClienteDocFiscal());
			JpaTelefone jpaTelefone = new JpaTelefone(aggregate.getAtendimento().getClienteTelefone().numero(),
					aggregate.getAtendimento().getClienteTelefone().tipo());
			JpaTelefone jpaWhatsapp = null;
			if (aggregate.getAtendimento().getClienteWhatsapp() == null) {

			} else {
				jpaWhatsapp = new JpaTelefone(aggregate.getAtendimento().getClienteWhatsapp().numero(),
						aggregate.getAtendimento().getClienteWhatsapp().tipo());
			}
			jpaAtendimentoProjection.setClienteTelefone(jpaTelefone);
			jpaAtendimentoProjection.setClienteWhatsapp(jpaWhatsapp);
		} else {
			jpaAtendimentoProjection.setClienteId(aggregate.getAtendimento().getClienteId().toString());
		}

		jpaAtendimentoProjection.setVeiculoPlaca(aggregate.getAtendimento().getVeiculoPlaca());
		jpaAtendimentoProjection.setTipoOcorrencia(aggregate.getAtendimento().getTipoOcorrencia());
		jpaAtendimentoProjection.setServicoId(aggregate.getAtendimento().getServicoId().toString());
		jpaAtendimentoProjection.setServicoNome(aggregate.getAtendimento().getServicoNome());
		jpaAtendimentoProjection.setDataHoraSolicitado(aggregate.getAtendimento().getDataHoraSolicitado());
		jpaAtendimentoProjection.setDataHoraConfirmado(aggregate.getAtendimento().getDataHoraConfirmado());
		jpaAtendimentoProjection.setDataHoraAjustado(aggregate.getAtendimento().getDataHoraAjustado());
		jpaAtendimentoProjection.setDataHoraFinalizado(aggregate.getAtendimento().getDataHoraFinalizado());
		jpaAtendimentoProjection.setDataHoraCancelado(aggregate.getAtendimento().getDataHoraCancelado());
		jpaAtendimentoProjection.setDescricao(aggregate.getAtendimento().getDescricao());

		JpaEndereco jpaEnderecoBase = new JpaEndereco();
		jpaEnderecoBase.setLogradouro(aggregate.getAtendimento().getBase().logradouro());
		jpaEnderecoBase.setNumero(aggregate.getAtendimento().getBase().numero());
		jpaEnderecoBase.setComplemento(aggregate.getAtendimento().getBase().complemento());
		jpaEnderecoBase.setBairro(aggregate.getAtendimento().getBase().bairro());
		jpaEnderecoBase.setCidade(aggregate.getAtendimento().getBase().cidade());
		jpaEnderecoBase.setEstado(aggregate.getAtendimento().getBase().estado());
		jpaEnderecoBase.setCep(aggregate.getAtendimento().getBase().cep());
		jpaAtendimentoProjection.setBase(jpaEnderecoBase);

		JpaEndereco jpaEnderecoOrigem = new JpaEndereco();
		jpaEnderecoOrigem.setLogradouro(aggregate.getAtendimento().getOrigem().logradouro());
		jpaEnderecoOrigem.setNumero(aggregate.getAtendimento().getOrigem().numero());
		jpaEnderecoOrigem.setComplemento(aggregate.getAtendimento().getOrigem().complemento());
		jpaEnderecoOrigem.setBairro(aggregate.getAtendimento().getOrigem().bairro());
		jpaEnderecoOrigem.setCidade(aggregate.getAtendimento().getOrigem().cidade());
		jpaEnderecoOrigem.setEstado(aggregate.getAtendimento().getOrigem().estado());
		jpaEnderecoOrigem.setCep(aggregate.getAtendimento().getOrigem().cep());
		jpaAtendimentoProjection.setOrigem(jpaEnderecoOrigem);

		if (aggregate.getAtendimento().getDestino() == null) {

		} else {
			JpaEndereco jpaEnderecoDestino = new JpaEndereco();
			jpaEnderecoDestino.setLogradouro(aggregate.getAtendimento().getDestino().logradouro());
			jpaEnderecoDestino.setNumero(aggregate.getAtendimento().getDestino().numero());
			jpaEnderecoDestino.setComplemento(aggregate.getAtendimento().getDestino().complemento());
			jpaEnderecoDestino.setBairro(aggregate.getAtendimento().getDestino().bairro());
			jpaEnderecoDestino.setCidade(aggregate.getAtendimento().getDestino().cidade());
			jpaEnderecoDestino.setEstado(aggregate.getAtendimento().getDestino().estado());
			jpaEnderecoDestino.setCep(aggregate.getAtendimento().getDestino().cep());
			jpaAtendimentoProjection.setDestino(jpaEnderecoDestino);
		}

		jpaAtendimentoProjection.setItems(aggregate.getAtendimento().getItems().stream().map(item -> {
			return new JpaItemProjection(item.getNome(), item.getUnidadeDeMedida(), item.getPrecoUnitario(),
					item.getQuantidade(), item.getObservacao());
		}).toList());

		this.jpaOrderProjectionRepository.save(jpaAtendimentoProjection);
	}

	@Nonnull
	@Override
	public String getAggregateType() {
		return AggregateType.ATENDIMENTO.toString();
	}
}
