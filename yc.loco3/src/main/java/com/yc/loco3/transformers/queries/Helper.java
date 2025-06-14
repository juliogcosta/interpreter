package com.yc.loco3.transformers.queries;

import org.json.JSONObject;

public class Helper
{
    public static enum Index {SELECT, FROM, WHERE, ORDER};

    public String query = null;

    public String dbType = "sql";

    public Boolean simpleQuery = false;

    public JSONObject clausula = new JSONObject();

    public JSONObject schema = null;

    //public JSONObject statementMapping = new JSONObject();
}
