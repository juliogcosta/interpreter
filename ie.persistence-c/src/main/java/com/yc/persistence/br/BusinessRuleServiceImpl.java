package com.yc.persistence.br;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.yc.loco3.transformers.BusinessRuleService;

@Service
public class BusinessRuleServiceImpl implements BusinessRuleService
{
    Logger logger = LoggerFactory.getLogger(getClass());

    private RestTemplate rest;

    private static String ContentType = "Content-Type";
    private static String XCommandAction = "X-Command-Action";
    private static String XAroundAction = "X-Around-Action";
    private static String applicationJSON = "application/json";

    public BusinessRuleServiceImpl()
    {
        this.rest = new RestTemplate();
    }

    @Override
    public JSONObject brCall(final String uri, String around, String action, JSONObject data) throws Exception
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set(BusinessRuleServiceImpl.ContentType, applicationJSON);
        headers.set(BusinessRuleServiceImpl.XCommandAction, action);
        headers.set(BusinessRuleServiceImpl.XAroundAction, around);

        HttpEntity<String> request = new HttpEntity<>(data.toString(), headers);
        ResponseEntity<String> response = this.rest.postForEntity(uri, request, String.class);

        if (response == null) 
        {
            throw new Exception("500:Erro interno com causa desconhecida.");
        }
        else if (response.getStatusCode() != HttpStatus.OK)
        {
            throw new Exception(String.valueOf(response.getStatusCode().value()).concat(":").concat(response.getBody()));
        }
        else 
        {
            try
            {
                return new JSONObject(response.getBody());
            }
            catch (Exception e)
            {
                throw new Exception(e);
            }
        }
    }
}