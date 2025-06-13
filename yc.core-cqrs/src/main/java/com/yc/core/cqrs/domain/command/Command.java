package com.yc.core.cqrs.domain.command;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yc.core.common.infrastructure.exception.IncompleteRegisterException;
import com.yc.core.cqrs.C;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
public class Command {

    protected final UUID aggregateId;
    protected final String aggregateType;
    protected final JsonNode commandData;
    protected final JsonNode commandModel;

    public Command(String aggregateType, JsonNode commandData, JsonNode commandModel)
            throws IncompleteRegisterException {
        this.validateDataAgainstModel(commandData, commandModel);

        ObjectNode commandDataCopy = commandData.deepCopy();

        Set<String> fieldsToRemove = new HashSet<>();
        Iterator<String> fieldNames = commandDataCopy.fieldNames();
        log.info("\n command.attributes: {}", commandModel.get(C.attribute));
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            if (fieldName.equals("id")) {

            } else if (commandModel.get(C.attribute).has(fieldName)) {

            } else
                fieldsToRemove.add(fieldName);
        }
        fieldsToRemove.forEach(commandDataCopy::remove);

        log.info("\n commandDataCopy: {}", commandDataCopy);
        log.info("\n commandDataCopy.id: {}", commandDataCopy.get(C.id));
        if (commandDataCopy.has(C.id)) {
            this.aggregateId = UUID.fromString(commandDataCopy.get(C.id).asText());
        } else if (((ArrayNode) commandModel.get("stateControl")).size() == 0) {
            this.aggregateId = UUID.randomUUID();
        } else
            throw new IncompleteRegisterException("O campo id está ausente ou nulo.");
        this.aggregateType = aggregateType;
        this.commandData = commandDataCopy;
        this.commandModel = commandModel;
    }

    private void validateDataAgainstModel(JsonNode aggregateData, JsonNode commandModel)
            throws IncompleteRegisterException {
        Iterator<Map.Entry<String, JsonNode>> attributes = commandModel.get(C.attribute).fields();
        while (attributes.hasNext()) {
            Map.Entry<String, JsonNode> attribute = attributes.next();
            String attributeName = attribute.getKey();
            JsonNode attributeSpec = attribute.getValue();
            String attributeType = attributeSpec.has(C.type) ? attributeSpec.get(C.type).asText() : null;
            JsonNode attributeValue = aggregateData.get(attributeName);

            if (attributeName.equals("status")) {

            } else if (attributeSpec.get(C.nullable).asBoolean()) {

            } else if (aggregateData.has(attributeName)) {
                if (aggregateData.get(attributeName).isNull()) {
                    throw new IncompleteRegisterException(
                            "Campo obrigatório ".concat(attributeName).concat(" ausente ou nulo."));
                }
            } else
                throw new IncompleteRegisterException(
                        "Campo obrigatório ".concat(attributeName).concat(" ausente ou nulo."));

            if (String.class.getSimpleName().equalsIgnoreCase(attributeType)) {
                if (attributeValue != null && !attributeValue.isNull()) {
                    int attributeValueMaxLength = attributeSpec.has(C.length) ? attributeSpec.get(C.length).asInt()
                            : -1;
                    String value = attributeValue.asText();
                    if (attributeValueMaxLength > 0 && value.length() > attributeValueMaxLength) {
                        throw new IllegalArgumentException(
                                "Campo '".concat(attributeName).concat("' excede o tamanho máximo de ")
                                        .concat(String.valueOf(attributeValueMaxLength)).concat(" caracteres."));
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
