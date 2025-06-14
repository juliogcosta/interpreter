package com.yc.persistence.api.client;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DBNDriverRemoteImpl 
{
	@Autowired
	private DBNDriverRESTClient clientRest;

	public JSONArray select(String traceId, String spanId, String parentSpanId, String orgName, String projectName, String keyspaceName, String sqlQuery) throws Exception
	{
		final ResponseEntity<String> responseEntity = this.clientRest.readData(traceId, spanId, parentSpanId, orgName, projectName, keyspaceName, sqlQuery);
		if (responseEntity.getStatusCode() == HttpStatus.OK)
		{
			return new JSONObject(responseEntity.getBody()).getJSONArray("data");
		}
		else
		{
			throw new Exception(responseEntity.getBody());
		}
	}

	public String toInsert(String traceId, String spanId, String parentSpanId, String orgName, String projectName, String keyspaceName, String data) throws Exception
	{
		final ResponseEntity<String> responseEntity = this.clientRest.insertData(traceId, spanId, parentSpanId, orgName, projectName, keyspaceName, data.toString());
		if (responseEntity.getStatusCode() == HttpStatus.CREATED)
		{
			return responseEntity.getBody();
		}
		else
		{
			throw new Exception(responseEntity.getBody());
		}
	}

	public void toUpdate(String traceId, String spanId, String parentSpanId, String orgName, String projectName, String keyspaceName, String data) throws Exception
	{
		final ResponseEntity<String> responseEntity = this.clientRest.updateData(traceId, spanId, parentSpanId, orgName, projectName, keyspaceName, data.toString());
		if (responseEntity.getStatusCode() != HttpStatus.OK)
		{
			throw new Exception(responseEntity.getBody());
		}
	}

	public void toDelete(String traceId, String spanId, String parentSpanId, String orgName, String projectName, String keyspaceName, String data) throws Exception
	{
		final ResponseEntity<String> responseEntity = this.clientRest.deleteData(traceId, spanId, parentSpanId, orgName, projectName, keyspaceName, data.toString());
		if (responseEntity.getStatusCode() != HttpStatus.OK)
		{
			throw new Exception(responseEntity.getBody());
		}
	}
}
