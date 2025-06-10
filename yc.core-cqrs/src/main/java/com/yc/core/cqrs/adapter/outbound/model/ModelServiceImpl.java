package com.yc.core.cqrs.adapter.outbound.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class ModelServiceImpl implements ModelService {

	private final ObjectMapper objectMapper;
	private final Map<String, ObjectNode> objectNodes;
	
	public ModelServiceImpl(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;

		ObjectNode clienteId = this.objectMapper.createObjectNode();
		clienteId.put("type", String.class.getName());
		clienteId.put("length", 64);
		clienteId.put("nullable", false);

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
		
		ObjectNode attributes = this.objectMapper.createObjectNode();
		attributes.set("clienteId", clienteId);
		attributes.set("clienteTipoDeDocFiscal", clienteTipoDeDocFiscal);
		attributes.set("clienteDocFiscal", clienteDocFiscal);
		attributes.set("clienteNome", clienteNome);
		attributes.set("clienteTelefoneNumero", clienteTelefoneNumero);
		attributes.set("clienteTelefoneTipo", clienteTelefoneTipo);
		attributes.set("clienteWhatsappNumero", clienteWhatsappNumero);
		attributes.set("clienteWhatsappTipo", clienteWhatsappTipo);
		attributes.set("veiculoPlaca", veiculoPlaca);
		attributes.set("tipoOcorrencia", tipoOcorrencia);
		attributes.set("servicoId", servicoId);
		attributes.set("servicoNome", servicoNome);
		attributes.set("baseEnderecoLogradouro", baseEnderecoLogradouro);
		attributes.set("baseEnderecoNumero", baseEnderecoNumero);
		attributes.set("baseEnderecoComplemento", baseEnderecoComplemento);
		attributes.set("baseEnderecoBairro", baseEnderecoBairro);
		attributes.set("baseEnderecoCidade", baseEnderecoCidade);
		attributes.set("baseEnderecoEstado", baseEnderecoEstado);
		attributes.set("baseEnderecoCep", baseEnderecoCep);
		attributes.set("origemEnderecoLogradouro", origemEnderecoLogradouro);
		attributes.set("origemEnderecoNumero", origemEnderecoNumero);
		attributes.set("origemEnderecoComplemento", origemEnderecoComplemento);
		attributes.set("origemEnderecoBairro", origemEnderecoBairro);
		attributes.set("origemEnderecoCidade", origemEnderecoCidade);
		attributes.set("origemEnderecoEstado", origemEnderecoEstado);
		attributes.set("origemEnderecoCep", origemEnderecoCep);
		
		ObjectNode solicitarAtendimento = this.objectMapper.createObjectNode();
		solicitarAtendimento.set("attribute", attributes);
		
		ObjectNode commandNode = this.objectMapper.createObjectNode();
		commandNode.set("SolicitarAtendimento", solicitarAtendimento);
		
		ObjectNode rootNode = this.objectMapper.createObjectNode();
		rootNode.put("schemaName", "atendimento_es");
		rootNode.set("command", commandNode);
		
		this.objectNodes = new HashMap<>();
		this.objectNodes.put("tenant", rootNode);
	}
	
	@Override
	public JsonNode getModel(String tenant) {
		return null;
	}

}
