package com.yc.loco3.transformers;

import org.json.JSONObject;

public interface BusinessRuleService
{
    public enum CallPoint {BEFORE, AFTER};

    public JSONObject brCall(String uri, String around, String action, JSONObject jsData) throws Exception;
}
