package com.yc.utils;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.yc.doc.endpoint.Endpoint;

public class Controller
{
    final public Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.application.name}")
    public String appName;

    @Value("${yc.api.management.doc.controllers}")
    public String controllers;

    @Value("${yc.api.management.server.log.control.display}")
    public Boolean logDisplay;

    @Value("${yc.api.management.server.log.control.data.persistence.ttl}")
    public Long ttl;

    public void evalLatency(Map<String, Long> map, long onStart)
    {
        if (map.get("count") < map.get("count-window"))
        {
            map.put("accumulated", map.get("accumulated") + (System.currentTimeMillis() - onStart));
            map.put("count", map.get("count") + 1);
            map.put("avg", map.get("accumulated") / map.get("count"));
        }
    }

    public ResponseEntity<String> getDoc()
    {
        JSONObject doc = new JSONObject();
        doc.put("service", new JSONObject());
        doc.getJSONObject("service").put("name", this.appName);
        doc.getJSONObject("service").put("description", "--");
        if (this.appName.equals("persistencec")) 
        {
            doc.getJSONObject("service").put("url_base", "/v2/persistence/c/");
        } 
        else if (this.appName.equals("persistenceq")) 
        {
            doc.getJSONObject("service").put("url_base", "/v2/persistence/q/");
        } 
        else 
        {
            doc.getJSONObject("service").put("url_base", "/v2/".concat(this.appName).concat("/"));
        }

        JSONObject endpoints = new JSONObject();
        //logger.info(" > controllers: "+controllers);
        Stream.of(controllers.split(Pattern.quote(";"))).forEach(controller -> {
            //logger.info(" > controller: "+controller);
            try {
                Stream.of(Class.forName(controller.trim()).getDeclaredMethods()).forEach(declaredMethod -> {
                    Stream.of(declaredMethod.getDeclaredAnnotations()).forEach(declaredAnnotation -> {
                        getEndpoint(endpoints, declaredMethod.getName(), declaredAnnotation);
                    });
                    if (endpoints.has(declaredMethod.getName()))
                    {
                        endpoints.getJSONObject(declaredMethod.getName()).put("code", new JSONObject());
                        endpoints.getJSONObject(declaredMethod.getName()).getJSONObject("code").put("class", controller);
                        endpoints.getJSONObject(declaredMethod.getName()).getJSONObject("code").put("method", new JSONObject());
                        endpoints.getJSONObject(declaredMethod.getName()).getJSONObject("code").getJSONObject("method").put("name", declaredMethod.getName());
                        endpoints.getJSONObject(declaredMethod.getName()).getJSONObject("code").getJSONObject("method").put("parameters", new JSONArray(declaredMethod.getParameters()));
                    }
                });
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        JSONArray jsArray = new JSONArray();
        endpoints.keySet().forEach(key -> {
            if (endpoints.getJSONObject(key).has("ignore") && endpoints.getJSONObject(key).getBoolean("ignore"))
            {
                
            }
            else jsArray.put(endpoints.getJSONObject(key));
        });

        doc.put("items", jsArray);
        return ResponseEntity.status(HttpStatus.OK).body(doc.toString(2));
    }
    
    private void getEndpoint(JSONObject endpoints, String key, Annotation declaredAnnotation)
    {
        JSONObject data = null;
        if (endpoints.has(key))
        {
            
        }
        else endpoints.put(key, data = new JSONObject());
        
        data = endpoints.getJSONObject(key);
        
        if (declaredAnnotation.annotationType().getCanonicalName().equals(PostMapping.class.getCanonicalName()))
        {
            PostMapping requestMapping = (PostMapping) declaredAnnotation;
            data.put("method", "POST");
            data.put("url", requestMapping.path());
            data.put("headers", new JSONArray(requestMapping.headers()));
            data.put("consumes", new JSONArray(requestMapping.consumes()));
            data.put("produces", new JSONArray(requestMapping.consumes()));
        }
        else if (declaredAnnotation.annotationType().getCanonicalName().equals(GetMapping.class.getCanonicalName()))
        {
            GetMapping requestMapping = (GetMapping) declaredAnnotation;
            data.put("method", "GET");
            data.put("url", requestMapping.path());
            data.put("headers", new JSONArray(requestMapping.headers()));
            data.put("consumes", new JSONArray(requestMapping.consumes()));
            data.put("produces", new JSONArray(requestMapping.consumes()));
        }
        else if (declaredAnnotation.annotationType().getCanonicalName().equals(PutMapping.class.getCanonicalName()))
        {
            PutMapping requestMapping = (PutMapping) declaredAnnotation;
            data.put("method", "PUT");
            data.put("url", requestMapping.path());
            data.put("headers", new JSONArray(requestMapping.headers()));
            data.put("consumes", new JSONArray(requestMapping.consumes()));
            data.put("produces", new JSONArray(requestMapping.consumes()));
        }
        else if (declaredAnnotation.annotationType().getCanonicalName().equals(DeleteMapping.class.getCanonicalName()))
        {
            DeleteMapping requestMapping = (DeleteMapping) declaredAnnotation;
            data.put("method", "DELETE");
            data.put("url", requestMapping.path());
            data.put("headers", new JSONArray(requestMapping.headers()));
            data.put("consumes", new JSONArray(requestMapping.consumes()));
            data.put("produces", new JSONArray(requestMapping.consumes()));
        }
        else if (declaredAnnotation.annotationType().getCanonicalName().equals(Endpoint.class.getCanonicalName()))
        {
            Endpoint requestMapping = (Endpoint) declaredAnnotation;
            data.put("name", requestMapping.name());
            data.put("description", requestMapping.description());
            data.put("errorStatusCode", new JSONArray(requestMapping.errorStatusCode()));
            data.put("successStatusCode", new JSONArray(requestMapping.successStatusCode()));
            data.put("bodyRequest", new JSONObject());
            data.getJSONObject("bodyRequest").put("format", requestMapping.bodyRequest().format());
            data.getJSONObject("bodyRequest").put("description", requestMapping.bodyRequest().description());
            data.put("bodyResponse", new JSONObject());
            data.getJSONObject("bodyResponse").put("formatOnSuccess", requestMapping.bodyResponse().formatOnSuccess());
            data.getJSONObject("bodyResponse").put("formatOnError", requestMapping.bodyResponse().formatOnError());
            data.getJSONObject("bodyResponse").put("description", requestMapping.bodyResponse().description());
            data.put("ignore", requestMapping.ignore());
        }
    }
}
