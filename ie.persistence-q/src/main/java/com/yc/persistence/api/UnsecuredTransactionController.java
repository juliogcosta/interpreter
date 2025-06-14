package com.yc.persistence.api;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.yc.models.nosql.Consumption;
import com.yc.persistence.exception.InterpreterControlledException;
import com.yc.persistence.service.QueryServiceImpl;
import com.yc.pr.models.User;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping(UnsecuredTransactionController.urlPrefix)
@RestController
public class UnsecuredTransactionController extends Controller
{
    final static public String urlPrefix = "/sm1on5480kgzp/unsecured";

    @Autowired
    protected QueryServiceImpl queryService;

    @PostMapping(path = "/s/ac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {
            HttpHeaders.CONTENT_TYPE, "X-Tenant-Id", HttpHeaders.AUTHORIZATION
    })
    public ResponseEntity<String> request(@RequestHeader(name = "Authorization", required = true) String authorization, 
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "Content-Length") long contentLength, 
            @RequestBody(required = true) String body, 
            HttpServletRequest request)
    {
        //logger.info(" > body: "+body);

        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (body.startsWith("{"))
        {
            return altRequest(authorization, tenantId, contentLength, user, body, request);
        }

        long onStart = System.currentTimeMillis();

        String responseBody = "";

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            //logger.info(" > -2");

            JSONArray bodyRequest = null;
            try
            {
                bodyRequest = new JSONArray(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            //logger.info(" > -1");

            //logger.info(" > bodyRequest: "+bodyRequest);
            JSONArray resp = this.queryService.readByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, bodyRequest, this.transform(user), true);
            int idx = 0;
            for ( ; idx < resp.length(); idx++)
            {
                //logger.info(" > b");

                JSONObject item = resp.getJSONObject(idx);
                if (item.getJSONArray(item.keys().next()).length() > 0)
                {
                    //logger.info(" > b.0");

                    break;
                }
            }

            //logger.info(" > 0");

            if (idx < resp.length())
            {
                return ResponseEntity.ok().body(resp.toString());
            }
            else
            {
                return ResponseEntity.noContent().build();
            }
        }
        catch (Exception e)
        {
            //logger.info(" > 1");

            try 
            {
                //logger.info(" > 1.0: message: "+e.getMessage()+", cause: "+ e.getCause()+", cause.message: "+(e.getCause() == null ? "cause:null" : e.getCause().getMessage()));

                e.printStackTrace();

                //logger.info(" > 1.1");

                /*JSONObject error = super.getErrorLogger().buildErrorMessage(tenantId, 
                        e, 
                        tenantId, 
                        xB3TraceId, 
                        xB3SpanId, 
                        xB3ParentSpanId, 
                        null, 
                        null, 
                        user.getUsername(), 
                        this.appName, 
                        request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString(), 
                        request.getMethod(), 
                        null, 
                        null, 
                        "i",
                        false);*/

                //logger.info(" > 1.2");

                //return ResponseEntity.status(error.getInt("code")).body(error.toString());
                return ResponseEntity.status(510).body("{\"message\":\"erro interno.\"}");
            } 
            catch (Exception ex) 
            {
                //logger.info(" > 1.3");

                ex.printStackTrace();
            }

            //logger.info(" > 2");

            return ResponseEntity.status(500).body("{\"message\":\"erro interno.\"}");
        }
        finally
        {
            //logger.info(" > 3");
            
            if (responseBody == null)
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()), 
                        contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
            else
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()),
                        responseBody.length() + contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
            
            //logger.info(" > 4");
        }
    }

    @PostMapping(path = "/s/no-ac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {
            HttpHeaders.CONTENT_TYPE, "X-Tenant-Id"
    })
    public ResponseEntity<String> request(@RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
            @RequestHeader(name = "Content-Length") long contentLength, 
            @RequestBody(required = true) String body,
            HttpServletRequest request)
    {
        //logger.info(" > body: "+body);

        if (body.startsWith("{"))
        {
            return altRequest(null, tenantId, contentLength, null, body, request);
        }

        long onStart = System.currentTimeMillis();

        String responseBody = "";

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            JSONArray bodyRequest = null;
            try
            {
                bodyRequest = new JSONArray(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            //logger.info(" > bodyRequest: "+bodyRequest);
            JSONArray resp = this.queryService.readByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, bodyRequest, null, true);
            int idx = 0;
            for ( ; idx < resp.length(); idx++)
            {
                JSONObject item = resp.getJSONObject(idx);
                if (item.getJSONArray(item.keys().next()).length() > 0)
                {
                    break;
                }
            }

            if (idx < resp.length())
            {
                return ResponseEntity.ok().body(resp.toString());
            }
            else
            {
                return ResponseEntity.noContent().build();
            }
        }
        catch (InterpreterControlledException e)
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).body(e.getMessage());
        }
        catch (Exception e)
        {
            //e.printStackTrace();

            JSONObject error = super.getErrorLogger().buildErrorMessage(tenantId, 
                    e, 
                    tenantId, 
                    xB3TraceId, 
                    xB3SpanId, 
                    xB3ParentSpanId, 
                    null, 
                    null, 
                    null, 
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString(), 
                    request.getMethod(), 
                    null, 
                    null, 
                    "i",
                    false);

            return ResponseEntity.status(error.getInt("code")).body(error.toString());
        }
        finally
        {
            if (responseBody == null)
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()), 
                        contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
            else
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()),
                        responseBody.length() + contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }

    @PostMapping(path = "/s/sttmnt", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {
            HttpHeaders.CONTENT_TYPE, "X-Tenant-Id", HttpHeaders.AUTHORIZATION
    })
    public ResponseEntity<String> requestStatement(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
            @RequestHeader(name = "Content-Length") long contentLength, 
            @RequestBody(required = true) String body,
            HttpServletRequest request)
    {
        //logger.info(" > body: "+body);

        long onStart = System.currentTimeMillis();

        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String responseBody = "";

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            JSONArray bodyRequest = null;
            try
            {
                bodyRequest = new JSONArray(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            JSONArray resp = this.queryService.readByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, bodyRequest, this.transform(user), false);
            int idx = 0;
            for ( ; idx < resp.length(); idx++)
            {
                JSONObject item = resp.getJSONObject(idx);
                if (item.getJSONArray(item.keys().next()).length() > 0)
                {
                    break;
                }
            }

            if (idx < resp.length())
            {
                return ResponseEntity.ok().body(resp.toString());
            }
            else
            {
                return ResponseEntity.noContent().build();
            }
        }
        catch (InterpreterControlledException e)
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).body(e.getMessage());
        }
        catch (Exception e)
        {
            //e.printStackTrace();

            JSONObject error = super.getErrorLogger().buildErrorMessage(tenantId, 
                    e, 
                    tenantId, 
                    xB3TraceId, 
                    xB3SpanId, 
                    xB3ParentSpanId, 
                    null, 
                    null, 
                    user.getUsername(), 
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString(), 
                    request.getMethod(), 
                    null, 
                    null, 
                    "i",
                    false);

            return ResponseEntity.status(error.getInt("code")).body(error.toString());
        }
        finally
        {
            if (responseBody == null)
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()), 
                        contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
            else
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()),
                        responseBody.length() + contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }

    private ResponseEntity<String> altRequest(String authorization, String tenantId, long contentLength, User user, String body, HttpServletRequest request)
    {
        long onStart = System.currentTimeMillis();

        String responseBody = "";

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            JSONObject bodyRequest = new JSONObject(body);
            try
            {
                bodyRequest = new JSONObject(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            JSONArray resp = this.queryService.readAltByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, bodyRequest, user == null ? null : this.transform(user), true);
            int idx = 0;
            for ( ; idx < resp.length(); idx++)
            {
                JSONObject item = resp.getJSONObject(idx);
                if (item.getJSONArray(item.keys().next()).length() > 0)
                {
                    break;
                }
            }

            if (idx < resp.length())
            {
                return ResponseEntity.ok().body(resp.toString());
            }
            else
            {
                return ResponseEntity.noContent().build();
            }
        }
        catch (InterpreterControlledException e)
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).body(e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();

            JSONObject error = super.getErrorLogger().buildErrorMessage(tenantId, 
                    e, 
                    tenantId, 
                    xB3TraceId, 
                    xB3SpanId, 
                    xB3ParentSpanId, 
                    null, 
                    null, 
                    user == null ? null : user.getUsername(), 
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString(), 
                    request.getMethod(), 
                    null, 
                    null, 
                    "i",
                    false);

            return ResponseEntity.status(error.getInt("code")).body(error.toString());
        }
        finally
        {
            if (responseBody == null)
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()), 
                        contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
            else
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()),
                        responseBody.length() + contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }

    private com.yc.persistence.model.CUser transform(User user)
    {
        final Set<br.edu.ufrn.loco3.security.Role> roles = new HashSet<br.edu.ufrn.loco3.security.Role>();
        user.getRoles().forEach(role -> roles.add(new com.yc.persistence.model.CRole(role.getName())));

        //roles.forEach(role -> { logger.info(" > role: "+role.getName()); });

        return new com.yc.persistence.model.CUser(user.getUsername(), roles);
    }
}
