package com.yc.persistence.service.br;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.yc.persistence.api.STransactionController;

@Service
public class ContractByPersistenceService
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private STransactionController sTransactionController;

    @Value("${yc.baas.project.contract.x-tenant-id}")
    private String XTenantID;

    @Value("${yc.baas.project.contract.x-tenant-secret}")
    private String XTenantSecret;

    public ContractByPersistenceService()
    {

    }

    public JSONArray findContractByOrgName(String orgNane) throws Exception
    {
        if (orgNane == null) return null;

        JSONObject jsContract = new JSONObject();
        jsContract.put("id", "");
        jsContract.put("org", orgNane);
        jsContract.put("status", "");
        jsContract.put("uuid", "");
        jsContract.put("hiredin", "");
        jsContract.put("expiresin", "");
        jsContract.put("logversion", "");
        jsContract.put("plan", new JSONObject());
        jsContract.getJSONObject("plan").put("id", "");
        jsContract.getJSONObject("plan").put("name", "");
        jsContract.getJSONObject("plan").put("ttl", "");
        jsContract.getJSONObject("plan").put("threshold", "");
        jsContract.getJSONObject("plan").put("ppo", "");
        jsContract.getJSONObject("plan").put("logversion", "");
        JSONObject jsData = new JSONObject();
        jsData.put("contract", jsContract);

        JSONArray jsQuery = new JSONArray();
        jsQuery.put(jsData);

        ResponseEntity<String> response = sTransactionController.openRequest(
                this.XTenantID, 
                this.XTenantSecret, 
                jsQuery.toString().length(), 
                jsQuery.toString(), 
                null);

        if (response == null) 
        {
            throw new Exception("500:Erro interno com causa desconhecida.");
        }
        else if (response.getStatusCodeValue() != HttpStatus.OK.value())
        {
            throw new Exception(response.getBody());
        }
        else 
        {
            JSONArray datas = null;

            try
            {
                datas = new JSONArray(response.getBody());
            }
            catch (Exception e)
            {
                throw new Exception(e);
            }

            if (datas.length() > 0 && datas.getJSONObject(0).has("contract")) 
            {
                return datas.getJSONObject(0).getJSONArray("contract");
            }
            else
            {
                throw new Exception("400:Contracto de org '".concat(orgNane).concat("' não localizado."));
            }
        }
    }
}