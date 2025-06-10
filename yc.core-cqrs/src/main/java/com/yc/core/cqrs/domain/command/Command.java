package com.yc.core.cqrs.domain.command;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
public class Command {

	protected final UUID aggregateId;
	protected final String aggregateType;
    protected final JsonNode commandData;
    protected final JsonNode commandModel;

    public Command(JsonNode aggregate, JsonNode commandModel) throws IncompleteRegisterException {
        this.validateDataAgainstModel(aggregate.get("data"), commandModel);

        ObjectNode aggregateCopy = aggregate.deepCopy();

        aggregateCopy.fieldNames().forEachRemaining(field -> {
            if (!"id".equals(field) && "type".equals(field) && !"data".equals(field)) {
                aggregateCopy.remove(field);
            }
        });

        if (aggregateCopy.has("data") && aggregateCopy.get("data").isObject()) {
            ObjectNode dataNode = (ObjectNode) aggregateCopy.get("data");
            Iterator<String> fieldNames = dataNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (!commandModel.has(fieldName) && !"type".equals(fieldName)) {
                    fieldNames.remove();
                }
            }
        }

        this.aggregateId = UUID.fromString(aggregateCopy.asText("id"));
        this.aggregateType = aggregateCopy.asText("type");
        this.commandData = aggregateCopy.get("data");
        this.commandModel = commandModel;
	}
    
    private void validateDataAgainstModel(JsonNode aggregateData, JsonNode commandModel) throws IncompleteRegisterException {
        Iterator<Map.Entry<String, JsonNode>> attributes = commandModel.get("attribute").fields();
        while (attributes.hasNext()) {
            Map.Entry<String, JsonNode> attribute = attributes.next();
            String attributeName = attribute.getKey();
            JsonNode attributeSpec = attribute.getValue();
            String attributeType = attributeSpec.has("type") ? attributeSpec.get("type").asText() : null;
            JsonNode attributeValue = aggregateData.get(attributeName);

            if (attributeSpec.get("nullable").asBoolean()) {
                
            } else if (aggregateData.has(attributeName)) {
            	if (aggregateData.get(attributeName).isNull()) {
            		throw new IncompleteRegisterException("Campo obrigatório ausente ou nulo: " + attributeName); 
                } 
            } else throw new IncompleteRegisterException("Campo obrigatório ausente ou nulo: " + attributeName); 
            
            if (String.class.getSimpleName().equalsIgnoreCase(attributeType)) {
                if (attributeValue != null && !attributeValue.isNull()) {
                    int attributeValueMaxLength = attributeSpec.has("length") ? attributeSpec.get("length").asInt() : -1;
                    String value = attributeValue.asText();
                    if (attributeValueMaxLength > 0 && value.length() > attributeValueMaxLength) {
                        throw new IllegalArgumentException("Campo '" + attributeName + "' excede o tamanho máximo de " + attributeValueMaxLength + " caracteres.");
                    }
                }
            }
        }
    }

    final public UUID getAggregateId() {
		return this.aggregateId;
	}

    final public String getAggregateType() {
    	return this.aggregateType;
    }
    
    final public JsonNode getAggregateData() {
		return this.commandData;
	}
    
    final public JsonNode getCommandModel() {
    	return this.commandModel;
    }
}
