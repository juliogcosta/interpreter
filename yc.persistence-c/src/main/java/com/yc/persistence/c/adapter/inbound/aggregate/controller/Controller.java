package com.yc.persistence.c.adapter.inbound.aggregate.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.core.cqrs.C;
import com.yc.core.cqrs.adapter.outbound.model.ModelService;
import com.yc.core.cqrs.application.service.command.processor.CommandProcessor;
import com.yc.core.cqrs.domain.command.Command;

import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IllegalRequestFormatException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Controller {
	
	private final ObjectMapper objectMapper;
	private final CommandProcessor commandProcessor;
	private final ModelService modelService;
	
	@PostMapping("/command")
	public ResponseEntity<JsonNode> createCommand(@RequestBody JsonNode request)
			throws IOException, ControlledException, IncompleteRegisterException, IllegalRequestFormatException {
		log.info("\n > request: {}", request);
		JSONObject jsInfo = this.validateRequestSintax(request);
		String aggregateType = jsInfo.getString(C.aggregateType);
		String aggregateCommand = jsInfo.getString(C.aggregateCommand);
		JsonNode commandModel = this.modelService.getModel(C.tenant).get(aggregateType).get(C.command).get(aggregateCommand);
        JsonNode commandData = request.get(jsInfo.getString(C.aggregateType)).get(jsInfo.getString(C.aggregateCommand));
        log.info("\n > aggregateType: {}", aggregateType);
        log.info("\n > aggregateCommand: {}", aggregateCommand);
        log.info("\n > commandModel: {}", commandModel);
        log.info("\n > commandData: {}", commandData);
        return ResponseEntity.ok(null);
        /*Command command = new Command(aggregateType, commandData, commandModel);
        JsonNode aggregateModel = this.modelService.getModel(C.tenant).get(aggregateType);
        var aggregate = this.commandProcessor.process(aggregateModel, command);
        return ResponseEntity.ok()
                .body(this.objectMapper.createObjectNode().put(C.id, aggregate.getAggregateId().toString()));*/
	}
	
	@PutMapping("/command")
	public ResponseEntity<JsonNode> updateCommand(@RequestBody JsonNode request)
			throws IOException, ControlledException, IncompleteRegisterException, IllegalRequestFormatException {
		JSONObject jsInfo = this.validateRequestSintax(request);

		String aggregateType = jsInfo.getString(C.aggregateType);
		String aggregateCommand = jsInfo.getString(C.aggregateCommand);
		JsonNode commandModel = this.modelService.getModel(C.tenant).get(aggregateType).get(C.command).get(aggregateCommand);
        JsonNode commandData = request.get(jsInfo.getString(C.aggregateType)).get(jsInfo.getString(C.aggregateCommand));
        Command command = new Command(aggregateType, commandData, commandModel);
        JsonNode aggregateModel = this.modelService.getModel(C.tenant).get(aggregateType);
        this.commandProcessor.process(aggregateModel, command);
        return ResponseEntity.ok().build();
	}
	
	private JSONObject validateRequestSintax(JsonNode request) throws IllegalRequestFormatException {
		JSONObject jsObject = new JSONObject();
		Iterator<Map.Entry<String, JsonNode>> iterator = request.fields();
		if (iterator.hasNext()) {
			Map.Entry<String, JsonNode> field = iterator.next();
			jsObject.put(C.aggregateType, field.getKey());
			if (this.modelService.getModel(C.tenant).has(jsObject.getString(C.aggregateType))) {
				iterator = field.getValue().fields();
				if (iterator.hasNext()) {
					field = iterator.next();
					jsObject.put(C.aggregateCommand, field.getKey());
					String aggregateType = jsObject.getString(C.aggregateType);
					String aggregateCommand = jsObject.getString(C.aggregateCommand);
					if (this.modelService.getModel(C.tenant).get(aggregateType).get(C.command).has(aggregateCommand)) {
						return jsObject;
					} else throw new IllegalRequestFormatException("Aparentemente o agregado não foi definido na requisição.");
				} else throw new IllegalRequestFormatException("Aparentemente o agregado não foi definido na requisição.");
			} else throw new IllegalRequestFormatException("Aparentemente o agregado não foi definido na requisição.");
		} else throw new IllegalRequestFormatException("Aparentemente o agregado não foi definido na requisição.");
	}
}
