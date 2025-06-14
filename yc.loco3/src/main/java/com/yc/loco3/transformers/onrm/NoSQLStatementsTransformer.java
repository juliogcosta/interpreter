package com.yc.loco3.transformers.onrm;

import org.json.JSONException;
import org.json.JSONObject;

public interface NoSQLStatementsTransformer
{
	public Boolean Single = false;
	public Boolean isCollection = !Single;
	public String CREATE = "create";
	public String DROP = "drop";
	public String INSERT = "insert";
    public String INSERT_ = "insert_";
    public String SELECT = "select";
    public String COUNT = "count";
	public String UPDATE = "update";
    public String UPDATE_ = "update_";
	public String DELETE = "delete";
	public String INHERITANCE = "inheritance";
	public String COLUMNS = "columns";

	public JSONObject reconfigureOOStts(final JSONObject model) throws Exception;

	public JSONObject getSttsFromOOStts(JSONObject clazzesModel) throws JSONException, Exception;
}