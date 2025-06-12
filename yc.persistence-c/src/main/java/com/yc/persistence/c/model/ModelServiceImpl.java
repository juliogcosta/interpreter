package com.yc.persistence.c.model;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yc.core.cqrs.adapter.outbound.model.ModelService;

@Service
public class ModelServiceImpl implements ModelService {

	private final ObjectMapper objectMapper;
	private final Map<String, ObjectNode> objectNodes;

	public ModelServiceImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		
		ObjectNode aggregates = this.objectMapper.createObjectNode();
		aggregates.set("Atendimento", buildAtendimentoAggregateRep());
		aggregates.set("Servico", buildServicoAggregateRep());
		
		this.objectNodes = new HashMap<>();
		this.objectNodes.put("tenant", aggregates);
	}
	
	private JsonNode buildServicoAggregateRep() {
		
		ObjectNode id = this.objectMapper.createObjectNode();
		id.put("type", String.class.getName());
		id.put("length", 64);
		id.put("nullable", true);
		
		ObjectNode version = this.objectMapper.createObjectNode();
		version.put("type", Long.class.getName());
		version.put("nullable", true);
		
		ObjectNode status = this.objectMapper.createObjectNode();
		status.put("type", String.class.getName());
		status.put("length", 16);
		status.put("nullable", false);
		
		ObjectNode nome = this.objectMapper.createObjectNode();
		nome.put("type", String.class.getName());
		nome.put("length", 16);
		nome.put("nullable", false);
		
		ObjectNode descricao = this.objectMapper.createObjectNode();
		descricao.put("type", "Text");
		descricao.put("nullable", true);
		
		ObjectNode certificacaoIso = this.objectMapper.createObjectNode();
		certificacaoIso.put("type", "Boolean");
		certificacaoIso.put("nullable", false);
		
		
		ObjectNode servicoEntityAttributes = this.objectMapper.createObjectNode();
		servicoEntityAttributes.set("id", id);
		servicoEntityAttributes.set("version", version);
		servicoEntityAttributes.set("status", status);
		servicoEntityAttributes.set("nome", nome);
		servicoEntityAttributes.set("descricao", descricao);
		servicoEntityAttributes.set("certificacaoIso", certificacaoIso);

		ObjectNode servicoEntity = this.objectMapper.createObjectNode();
		servicoEntity.set("attributes", servicoEntityAttributes);
		
		
		
		/**
		 * Sobre Comandos a respeito de instância de Servico
		 * 
		 */
		/**
		 * 1. Comando Disponibilizar
		 * 
		 */
		
		ObjectNode disponibilizarServicoCommandAttributes = this.objectMapper.createObjectNode();
		disponibilizarServicoCommandAttributes.set("id", id);
		disponibilizarServicoCommandAttributes.set("version", version);
		disponibilizarServicoCommandAttributes.set("status", status);
		disponibilizarServicoCommandAttributes.set("nome", nome);
		disponibilizarServicoCommandAttributes.set("descricao", descricao);
		disponibilizarServicoCommandAttributes.set("certificacaoIso", certificacaoIso);
		
		ObjectNode disponibilizarServicoCommand = this.objectMapper.createObjectNode();
		disponibilizarServicoCommand.set("attribute", disponibilizarServicoCommandAttributes);
		disponibilizarServicoCommand.put("endState", "Disponibilizado");
		
		ArrayNode disponibilizarServicoCommandAllowedStatus = this.objectMapper.createArrayNode();
		disponibilizarServicoCommand.set("stateControl", disponibilizarServicoCommandAllowedStatus);
		
		/**
		 * 2. Comando Cancelar
		 * 
		 */
		
		ObjectNode cancelarServicoCommandAttributes = this.objectMapper.createObjectNode();
				
		ObjectNode cancelarServicoCommand = this.objectMapper.createObjectNode();
		cancelarServicoCommand.set("attribute", cancelarServicoCommandAttributes);
		cancelarServicoCommand.put("endState", "Cancelado");
		
		ArrayNode cancelarServicoCommandAllowedStatus = this.objectMapper.createArrayNode();
		Stream.of(new String [] {"DISPONIBILIZADO"})
				.forEach(item -> cancelarServicoCommandAllowedStatus.add(item));
		cancelarServicoCommand.set("stateControl", cancelarServicoCommandAllowedStatus);
		
		
		/**
		 * Definicao de quais comandos compoem o agregado
		 * 
		 */
		
		ObjectNode commands = this.objectMapper.createObjectNode();
		commands.set("Disponibilizar", disponibilizarServicoCommand);
		commands.set("Cancelar", cancelarServicoCommand);

		
		
		/**
		 * Sobre Eventos a respeito de instância de Servico
		 * 
		 */
		/**
		 * 1. Evento Disponibilizado
		 * 
		 */
		
		ObjectNode servicoDisponibilizadoEvent = this.objectMapper.createObjectNode();
		servicoDisponibilizadoEvent.set("attributes", disponibilizarServicoCommandAttributes);
		servicoDisponibilizadoEvent.put("type", "Disponibilizado");
		servicoDisponibilizadoEvent.put("status", "Disponibilizado");

		/**
		 * 2. Evento Cancelado
		 * 
		 */
		
		ObjectNode servicoCanceladoEvent = this.objectMapper.createObjectNode();
		servicoCanceladoEvent.set("attributes", cancelarServicoCommandAttributes);
		servicoCanceladoEvent.put("type", "Cancelado");
		servicoCanceladoEvent.put("status", "Cancelado");
		
		
		/**
		 * Definicao de quais eventos compoem o agregado
		 * 
		 */
		
		ObjectNode events = this.objectMapper.createObjectNode();
		events.set("Disponibilizado", servicoDisponibilizadoEvent);
		events.set("Cancelado", servicoCanceladoEvent);
		
		
		/**
		 * Definicao de quais entidades compoem o agregado
		 * 
		 */
		
		ObjectNode entities = this.objectMapper.createObjectNode();
		entities.set("servico", servicoEntity);
		
		
		/**
		 * Definição do agregado
		 * 
		 */
		
		ObjectNode servicoAggregateSchema = this.objectMapper.createObjectNode();
		servicoAggregateSchema.put("name", "atendimento_es");
		
		ObjectNode servicoAggregate = this.objectMapper.createObjectNode();
		servicoAggregate.set("schema", servicoAggregateSchema);
		servicoAggregate.put("type", "Servico");
		servicoAggregate.set("command", commands);
		servicoAggregate.set("event", events);
		servicoAggregate.set("entity", entities);
		
		return servicoAggregate;
	}
	
	public JsonNode buildAtendimentoAggregateRep() {

		/**
		 * Sobre a entidade Atendimento
		 * 
		 */

		ObjectNode id = this.objectMapper.createObjectNode();
		id.put("type", String.class.getName());
		id.put("length", 64);
		id.put("nullable", true);

		ObjectNode version = this.objectMapper.createObjectNode();
		version.put("type", Long.class.getName());
		version.put("nullable", true);

		ObjectNode status = this.objectMapper.createObjectNode();
		status.put("type", String.class.getName());
		status.put("length", 16);
		status.put("nullable", false);

		ObjectNode prestadorId = this.objectMapper.createObjectNode();
		prestadorId.put("type", String.class.getName());
		prestadorId.put("length", 64);
		prestadorId.put("nullable", true);

		ObjectNode clienteId = this.objectMapper.createObjectNode();
		clienteId.put("type", String.class.getName());
		clienteId.put("length", 64);
		clienteId.put("nullable", true);

		ObjectNode clienteTipoDeDocFiscal = this.objectMapper.createObjectNode();
		clienteTipoDeDocFiscal.put("type", String.class.getName());
		clienteTipoDeDocFiscal.put("length", 8);
		clienteTipoDeDocFiscal.put("nullable", false);

		ObjectNode clienteDocFiscal = this.objectMapper.createObjectNode();
		clienteDocFiscal.put("type", String.class.getName());
		clienteDocFiscal.put("length", 16);
		clienteDocFiscal.put("nullable", false);

		ObjectNode clienteNome = this.objectMapper.createObjectNode();
		clienteNome.put("type", String.class.getName());
		clienteNome.put("length", 16);
		clienteNome.put("nullable", false);

		ObjectNode clienteTelefoneNumero = this.objectMapper.createObjectNode();
		clienteTelefoneNumero.put("type", String.class.getName());
		clienteTelefoneNumero.put("length", 16);
		clienteTelefoneNumero.put("nullable", false);

		ObjectNode clienteTelefoneTipo = this.objectMapper.createObjectNode();
		clienteTelefoneTipo.put("type", String.class.getName());
		clienteTelefoneTipo.put("length", 16);
		clienteTelefoneTipo.put("nullable", false);

		ObjectNode clienteWhatsappNumero = this.objectMapper.createObjectNode();
		clienteWhatsappNumero.put("type", String.class.getName());
		clienteWhatsappNumero.put("length", 16);
		clienteWhatsappNumero.put("nullable", true);

		ObjectNode clienteWhatsappTipo = this.objectMapper.createObjectNode();
		clienteWhatsappTipo.put("type", String.class.getName());
		clienteWhatsappTipo.put("length", 16);
		clienteWhatsappTipo.put("nullable", true);

		ObjectNode veiculoPlaca = this.objectMapper.createObjectNode();
		veiculoPlaca.put("type", String.class.getName());
		veiculoPlaca.put("length", 16);
		veiculoPlaca.put("nullable", false);

		ObjectNode tipoOcorrencia = this.objectMapper.createObjectNode();
		tipoOcorrencia.put("type", String.class.getName());
		tipoOcorrencia.put("length", 32);
		tipoOcorrencia.put("nullable", false);

		ObjectNode servicoId = this.objectMapper.createObjectNode();
		servicoId.put("type", String.class.getName());
		servicoId.put("length", 64);
		servicoId.put("nullable", false);

		ObjectNode servicoNome = this.objectMapper.createObjectNode();
		servicoNome.put("type", String.class.getName());
		servicoNome.put("length", 64);
		servicoNome.put("nullable", false);

		ObjectNode descricao = this.objectMapper.createObjectNode();
		descricao.put("type", "Text");
		descricao.put("nullable", true);

		ObjectNode baseEnderecoLogradouro = this.objectMapper.createObjectNode();
		baseEnderecoLogradouro.put("type", String.class.getName());
		baseEnderecoLogradouro.put("length", 128);
		baseEnderecoLogradouro.put("nullable", false);

		ObjectNode baseEnderecoNumero = this.objectMapper.createObjectNode();
		baseEnderecoNumero.put("type", String.class.getName());
		baseEnderecoNumero.put("length", 16);
		baseEnderecoNumero.put("nullable", false);

		ObjectNode baseEnderecoComplemento = this.objectMapper.createObjectNode();
		baseEnderecoComplemento.put("type", String.class.getName());
		baseEnderecoComplemento.put("length", 512);
		baseEnderecoComplemento.put("nullable", false);

		ObjectNode baseEnderecoBairro = this.objectMapper.createObjectNode();
		baseEnderecoBairro.put("type", String.class.getName());
		baseEnderecoBairro.put("length", 64);
		baseEnderecoBairro.put("nullable", false);

		ObjectNode baseEnderecoCidade = this.objectMapper.createObjectNode();
		baseEnderecoCidade.put("type", String.class.getName());
		baseEnderecoCidade.put("length", 64);
		baseEnderecoCidade.put("nullable", false);

		ObjectNode baseEnderecoEstado = this.objectMapper.createObjectNode();
		baseEnderecoEstado.put("type", String.class.getName());
		baseEnderecoEstado.put("length", 2);
		baseEnderecoEstado.put("nullable", false);

		ObjectNode baseEnderecoCep = this.objectMapper.createObjectNode();
		baseEnderecoCep.put("type", String.class.getName());
		baseEnderecoCep.put("length", 16);
		baseEnderecoCep.put("nullable", false);

		ObjectNode origemEnderecoLogradouro = this.objectMapper.createObjectNode();
		origemEnderecoLogradouro.put("type", String.class.getName());
		origemEnderecoLogradouro.put("length", 128);
		origemEnderecoLogradouro.put("nullable", false);

		ObjectNode origemEnderecoNumero = this.objectMapper.createObjectNode();
		origemEnderecoNumero.put("type", String.class.getName());
		origemEnderecoNumero.put("length", 16);
		origemEnderecoNumero.put("nullable", false);

		ObjectNode origemEnderecoComplemento = this.objectMapper.createObjectNode();
		origemEnderecoComplemento.put("type", String.class.getName());
		origemEnderecoComplemento.put("length", 512);
		origemEnderecoComplemento.put("nullable", false);

		ObjectNode origemEnderecoBairro = this.objectMapper.createObjectNode();
		origemEnderecoBairro.put("type", String.class.getName());
		origemEnderecoBairro.put("length", 64);
		origemEnderecoBairro.put("nullable", false);

		ObjectNode origemEnderecoCidade = this.objectMapper.createObjectNode();
		origemEnderecoCidade.put("type", String.class.getName());
		origemEnderecoCidade.put("length", 64);
		origemEnderecoCidade.put("nullable", false);

		ObjectNode origemEnderecoEstado = this.objectMapper.createObjectNode();
		origemEnderecoEstado.put("type", String.class.getName());
		origemEnderecoEstado.put("length", 2);
		origemEnderecoEstado.put("nullable", false);

		ObjectNode origemEnderecoCep = this.objectMapper.createObjectNode();
		origemEnderecoCep.put("type", String.class.getName());
		origemEnderecoCep.put("length", 16);
		origemEnderecoCep.put("nullable", false);

		ObjectNode destinoEnderecoLogradouro = this.objectMapper.createObjectNode();
		destinoEnderecoLogradouro.put("type", String.class.getName());
		destinoEnderecoLogradouro.put("length", 128);
		destinoEnderecoLogradouro.put("nullable", false);

		ObjectNode destinoEnderecoNumero = this.objectMapper.createObjectNode();
		destinoEnderecoNumero.put("type", String.class.getName());
		destinoEnderecoNumero.put("length", 16);
		destinoEnderecoNumero.put("nullable", false);

		ObjectNode destinoEnderecoComplemento = this.objectMapper.createObjectNode();
		destinoEnderecoComplemento.put("type", String.class.getName());
		destinoEnderecoComplemento.put("length", 512);
		destinoEnderecoComplemento.put("nullable", false);

		ObjectNode destinoEnderecoBairro = this.objectMapper.createObjectNode();
		destinoEnderecoBairro.put("type", String.class.getName());
		destinoEnderecoBairro.put("length", 64);
		destinoEnderecoBairro.put("nullable", false);

		ObjectNode destinoEnderecoCidade = this.objectMapper.createObjectNode();
		destinoEnderecoCidade.put("type", String.class.getName());
		destinoEnderecoCidade.put("length", 64);
		destinoEnderecoCidade.put("nullable", false);

		ObjectNode destinoEnderecoEstado = this.objectMapper.createObjectNode();
		destinoEnderecoEstado.put("type", String.class.getName());
		destinoEnderecoEstado.put("length", 2);
		destinoEnderecoEstado.put("nullable", false);

		ObjectNode destinoEnderecoCep = this.objectMapper.createObjectNode();
		destinoEnderecoCep.put("type", String.class.getName());
		destinoEnderecoCep.put("length", 16);
		destinoEnderecoCep.put("nullable", false);
		
		ObjectNode atendimentoEntityAttributes = this.objectMapper.createObjectNode();
		atendimentoEntityAttributes.set("id", id);
		atendimentoEntityAttributes.set("status", status);
		atendimentoEntityAttributes.set("version", version);
		atendimentoEntityAttributes.set("prestadorId", prestadorId);
		atendimentoEntityAttributes.set("clienteId", clienteId);
		atendimentoEntityAttributes.set("clienteTipoDeDocFiscal", clienteTipoDeDocFiscal);
		atendimentoEntityAttributes.set("clienteDocFiscal", clienteDocFiscal);
		atendimentoEntityAttributes.set("clienteNome", clienteNome);
		atendimentoEntityAttributes.set("clienteTelefoneNumero", clienteTelefoneNumero);
		atendimentoEntityAttributes.set("clienteTelefoneTipo", clienteTelefoneTipo);
		atendimentoEntityAttributes.set("clienteWhatsappNumero", clienteWhatsappNumero);
		atendimentoEntityAttributes.set("clienteWhatsappTipo", clienteWhatsappTipo);
		atendimentoEntityAttributes.set("veiculoPlaca", veiculoPlaca);
		atendimentoEntityAttributes.set("tipoOcorrencia", tipoOcorrencia);
		atendimentoEntityAttributes.set("servicoId", servicoId);
		atendimentoEntityAttributes.set("servicoNome", servicoNome);
		atendimentoEntityAttributes.set("baseEnderecoLogradouro", baseEnderecoLogradouro);
		atendimentoEntityAttributes.set("baseEnderecoNumero", baseEnderecoNumero);
		atendimentoEntityAttributes.set("baseEnderecoComplemento", baseEnderecoComplemento);
		atendimentoEntityAttributes.set("baseEnderecoBairro", baseEnderecoBairro);
		atendimentoEntityAttributes.set("baseEnderecoCidade", baseEnderecoCidade);
		atendimentoEntityAttributes.set("baseEnderecoEstado", baseEnderecoEstado);
		atendimentoEntityAttributes.set("baseEnderecoCep", baseEnderecoCep);
		atendimentoEntityAttributes.set("origemEnderecoLogradouro", origemEnderecoLogradouro);
		atendimentoEntityAttributes.set("origemEnderecoNumero", origemEnderecoNumero);
		atendimentoEntityAttributes.set("origemEnderecoComplemento", origemEnderecoComplemento);
		atendimentoEntityAttributes.set("origemEnderecoBairro", origemEnderecoBairro);
		atendimentoEntityAttributes.set("origemEnderecoCidade", origemEnderecoCidade);
		atendimentoEntityAttributes.set("origemEnderecoEstado", origemEnderecoEstado);
		atendimentoEntityAttributes.set("origemEnderecoCep", origemEnderecoCep);
		atendimentoEntityAttributes.set("destinoEnderecoLogradouro", destinoEnderecoLogradouro);
		atendimentoEntityAttributes.set("destinoEnderecoNumero", destinoEnderecoNumero);
		atendimentoEntityAttributes.set("destinoEnderecoComplemento", destinoEnderecoComplemento);
		atendimentoEntityAttributes.set("destinoEnderecoBairro", destinoEnderecoBairro);
		atendimentoEntityAttributes.set("destinoEnderecoCidade", destinoEnderecoCidade);
		atendimentoEntityAttributes.set("destinoEnderecoEstado", destinoEnderecoEstado);
		atendimentoEntityAttributes.set("destinoEnderecoCep", destinoEnderecoCep);
		atendimentoEntityAttributes.set("descricao", descricao);

		ObjectNode atendimentoEntity = this.objectMapper.createObjectNode();
		atendimentoEntity.set("attributes", atendimentoEntityAttributes);
		
		
		
		/**
		 * Sobre a entidade Item
		 * 
		 */

		ObjectNode nome = this.objectMapper.createObjectNode();
		nome.put("type", String.class.getName());
		nome.put("length", 64);
		nome.put("nullable", false);

		ObjectNode unidadeDeMedida = this.objectMapper.createObjectNode();
		unidadeDeMedida.put("type", String.class.getName());
		unidadeDeMedida.put("length", 64);
		unidadeDeMedida.put("nullable", false);

		ObjectNode precoUnitario = this.objectMapper.createObjectNode();
		precoUnitario.put("type", Integer.class.getName());
		precoUnitario.put("nullable", false);

		ObjectNode quantidade = this.objectMapper.createObjectNode();
		quantidade.put("type", Integer.class.getName());
		quantidade.put("nullable", false);

		ObjectNode observacao = this.objectMapper.createObjectNode();
		observacao.put("type", String.class.getName());
		observacao.put("length", 512);
		observacao.put("nullable", true);

		ObjectNode itemEntityAttributes = this.objectMapper.createObjectNode();
		itemEntityAttributes.set("nome", nome);
		itemEntityAttributes.set("unidadeDeMedida", unidadeDeMedida);
		itemEntityAttributes.set("precoUnitario", precoUnitario);
		itemEntityAttributes.set("quantidade", quantidade);
		itemEntityAttributes.set("observacao", observacao);

		ObjectNode itemEntityAssociations = this.objectMapper.createObjectNode();
		itemEntityAssociations.set("atendimento", atendimentoEntity);

		ObjectNode itemEntity = this.objectMapper.createObjectNode();
		itemEntity.set("attributes", itemEntityAttributes);
		itemEntity.set("associations", itemEntityAssociations);
		
		
		
		/**
		 * Sobre Comandos a respeito de instância de Atendimento
		 * 
		 */
		
		/**
		 * 1. Comando SolicitarAtendimento
		 * 
		 */
		
		ObjectNode solicitarAtendimentoCommandAttributes = this.objectMapper.createObjectNode();
		solicitarAtendimentoCommandAttributes.set("clienteId", clienteId);
		solicitarAtendimentoCommandAttributes.set("clienteTipoDeDocFiscal", clienteTipoDeDocFiscal);
		solicitarAtendimentoCommandAttributes.set("clienteDocFiscal", clienteDocFiscal);
		solicitarAtendimentoCommandAttributes.set("clienteNome", clienteNome);
		solicitarAtendimentoCommandAttributes.set("clienteTelefoneNumero", clienteTelefoneNumero);
		solicitarAtendimentoCommandAttributes.set("clienteTelefoneTipo", clienteTelefoneTipo);
		solicitarAtendimentoCommandAttributes.set("clienteWhatsappNumero", clienteWhatsappNumero);
		solicitarAtendimentoCommandAttributes.set("clienteWhatsappTipo", clienteWhatsappTipo);
		solicitarAtendimentoCommandAttributes.set("veiculoPlaca", veiculoPlaca);
		solicitarAtendimentoCommandAttributes.set("tipoOcorrencia", tipoOcorrencia);
		solicitarAtendimentoCommandAttributes.set("servicoId", servicoId);
		solicitarAtendimentoCommandAttributes.set("servicoNome", servicoNome);
		solicitarAtendimentoCommandAttributes.set("baseEnderecoLogradouro", baseEnderecoLogradouro);
		solicitarAtendimentoCommandAttributes.set("baseEnderecoNumero", baseEnderecoNumero);
		solicitarAtendimentoCommandAttributes.set("baseEnderecoComplemento", baseEnderecoComplemento);
		solicitarAtendimentoCommandAttributes.set("baseEnderecoBairro", baseEnderecoBairro);
		solicitarAtendimentoCommandAttributes.set("baseEnderecoCidade", baseEnderecoCidade);
		solicitarAtendimentoCommandAttributes.set("baseEnderecoEstado", baseEnderecoEstado);
		solicitarAtendimentoCommandAttributes.set("baseEnderecoCep", baseEnderecoCep);
		solicitarAtendimentoCommandAttributes.set("origemEnderecoLogradouro", origemEnderecoLogradouro);
		solicitarAtendimentoCommandAttributes.set("origemEnderecoNumero", origemEnderecoNumero);
		solicitarAtendimentoCommandAttributes.set("origemEnderecoComplemento", origemEnderecoComplemento);
		solicitarAtendimentoCommandAttributes.set("origemEnderecoBairro", origemEnderecoBairro);
		solicitarAtendimentoCommandAttributes.set("origemEnderecoCidade", origemEnderecoCidade);
		solicitarAtendimentoCommandAttributes.set("origemEnderecoEstado", origemEnderecoEstado);
		solicitarAtendimentoCommandAttributes.set("origemEnderecoCep", origemEnderecoCep);
		
		
		ObjectNode solicitarAtendimentoCommand = this.objectMapper.createObjectNode();
		solicitarAtendimentoCommand.set("attribute", solicitarAtendimentoCommandAttributes);
		solicitarAtendimentoCommand.put("endState", "Solicitado");
		
		ArrayNode solicitarAtendimentoCommandAllowedStatus = this.objectMapper.createArrayNode();
		solicitarAtendimentoCommand.set("stateControl", solicitarAtendimentoCommandAllowedStatus);
		

		/**
		 * 2. Comando AjustarAtendimento
		 * 
		 */
		
		ObjectNode ajustarAtendimentoCommandAttributes = this.objectMapper.createObjectNode();
		ajustarAtendimentoCommandAttributes.set("servicoId", servicoId);
		ajustarAtendimentoCommandAttributes.set("servicoNome", servicoNome);
		ajustarAtendimentoCommandAttributes.set("origemEnderecoLogradouro", origemEnderecoLogradouro);
		ajustarAtendimentoCommandAttributes.set("origemEnderecoNumero", origemEnderecoNumero);
		ajustarAtendimentoCommandAttributes.set("origemEnderecoComplemento", origemEnderecoComplemento);
		ajustarAtendimentoCommandAttributes.set("origemEnderecoBairro", origemEnderecoBairro);
		ajustarAtendimentoCommandAttributes.set("origemEnderecoCidade", origemEnderecoCidade);
		ajustarAtendimentoCommandAttributes.set("origemEnderecoEstado", origemEnderecoEstado);
		ajustarAtendimentoCommandAttributes.set("origemEnderecoCep", origemEnderecoCep);
		ajustarAtendimentoCommandAttributes.set("destinoEnderecoLogradouro", destinoEnderecoLogradouro);
		ajustarAtendimentoCommandAttributes.set("destinoEnderecoNumero", destinoEnderecoNumero);
		ajustarAtendimentoCommandAttributes.set("destinoEnderecoComplemento", destinoEnderecoComplemento);
		ajustarAtendimentoCommandAttributes.set("destinoEnderecoBairro", destinoEnderecoBairro);
		ajustarAtendimentoCommandAttributes.set("destinoEnderecoCidade", destinoEnderecoCidade);
		ajustarAtendimentoCommandAttributes.set("destinoEnderecoEstado", destinoEnderecoEstado);
		ajustarAtendimentoCommandAttributes.set("destinoEnderecoCep", destinoEnderecoCep);
		ajustarAtendimentoCommandAttributes.set("descricao", descricao);
		ajustarAtendimentoCommandAttributes.set("List.of(Item)", itemEntity);
		
		ObjectNode ajustarAtendimentoCommand = this.objectMapper.createObjectNode();
		ajustarAtendimentoCommand.set("attribute", ajustarAtendimentoCommandAttributes);
		ajustarAtendimentoCommand.put("endState", "Ajustado");
		
		ArrayNode ajustarAtendimentoCommandAllowedStatus = this.objectMapper.createArrayNode();
		Stream.of(new String [] {"SOLICITADO", "AJUSTADO"})
				.forEach(item -> ajustarAtendimentoCommandAllowedStatus.add(item));
		ajustarAtendimentoCommand.set("stateControl", ajustarAtendimentoCommandAllowedStatus);
		
		/**
		 * 3. Comando ConfirmarAtendimento
		 * 
		 */
		
		ObjectNode confirmarAtendimentoCommandAttributes = this.objectMapper.createObjectNode();
		confirmarAtendimentoCommandAttributes.set("prestadorId", prestadorId);
		
		ObjectNode confirmarAtendimentoCommand = this.objectMapper.createObjectNode();
		confirmarAtendimentoCommand.set("attribute", confirmarAtendimentoCommandAttributes);
		confirmarAtendimentoCommand.put("endState", "Confirmado");
		
		ArrayNode confirmarAtendimentoCommandAllowedStatus = this.objectMapper.createArrayNode();
		Stream.of(new String [] {"AJUSTADO"})
				.forEach(item -> confirmarAtendimentoCommandAllowedStatus.add(item));
		confirmarAtendimentoCommand.set("stateControl", confirmarAtendimentoCommandAllowedStatus);		
		
		/**
		 * Definicao de quais comandos compoem o agregado
		 * 
		 */
		
		ObjectNode commands = this.objectMapper.createObjectNode();
		commands.set("Solicitar", solicitarAtendimentoCommand);
		commands.set("Ajustar", ajustarAtendimentoCommand);
		commands.set("Confirmar", confirmarAtendimentoCommand);
		
		
		/**
		 * Definicao de quais comandos compoem o agregado
		 * 
		 */
		ObjectNode atendimentoSolicitadoEvent = this.objectMapper.createObjectNode();
		atendimentoSolicitadoEvent.set("attributes", solicitarAtendimentoCommandAttributes);
		atendimentoSolicitadoEvent.put("type", "Solicitado");
		atendimentoSolicitadoEvent.put("status", "Solicitado");
		
		ObjectNode atendimentoAjustadoEvent = this.objectMapper.createObjectNode();
		atendimentoAjustadoEvent.set("attributes", ajustarAtendimentoCommandAttributes);
		atendimentoAjustadoEvent.put("type", "Ajustado");
		atendimentoAjustadoEvent.put("status", "Ajustado");
		
		ObjectNode atendimentoConfirmadoEvent = this.objectMapper.createObjectNode();
		atendimentoConfirmadoEvent.set("attributes", confirmarAtendimentoCommandAttributes);
		atendimentoConfirmadoEvent.put("type", "Confirmado");
		atendimentoConfirmadoEvent.put("status", "Confirmado");
		
		ObjectNode events = this.objectMapper.createObjectNode();
		events.set("Solicitado", atendimentoSolicitadoEvent);
		events.set("Ajustado", atendimentoAjustadoEvent);
		events.set("Confirmado", atendimentoConfirmadoEvent);
		
		
		/**
		 * Definicao de quais entidades compoem o agregado
		 * 
		 */
		
		ObjectNode entities = this.objectMapper.createObjectNode();
		entities.set("atendimento", atendimentoEntity);
		entities.set("item", itemEntity);
		
		
		/**
		 * Definição do agregado
		 * 
		 */
		
		ObjectNode atendimentoAggregateSchema = this.objectMapper.createObjectNode();
		atendimentoAggregateSchema.put("name", "atendimento_es");
		
		ObjectNode atendimentoAggregate = this.objectMapper.createObjectNode();
		atendimentoAggregate.set("schema", atendimentoAggregateSchema);
		atendimentoAggregate.put("type", "Atendimento");
		atendimentoAggregate.set("command", commands);
		atendimentoAggregate.set("event", events);
		atendimentoAggregate.set("entity", entities);
		
		return atendimentoAggregate;
	}
	
	@Override
	public JsonNode getModel(String tenant) {
		return this.objectNodes.get(tenant);
	}
}
