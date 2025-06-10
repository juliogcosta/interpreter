package com.yc.core.cqrs.adapter.outbound.model;

import com.fasterxml.jackson.databind.JsonNode;

public interface ModelService {
	public JsonNode getModel(String tenant);
}
