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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.yc.loco3.Constant;
import com.yc.loco3.security.CredentialServiceImpl;
import com.yc.loco3.security.UserlessAuthorizationServiceImpl;
import com.yc.models.nosql.Consumption;
import com.yc.persistence.exception.InterpreterControlledException;
import com.yc.persistence.service.TransactionServiceImpl;
import com.yc.pr.models.User;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class TransactionController extends Controller
{
    @Autowired
    protected TransactionServiceImpl transactionService;
    
    @PostMapping(path = "/ac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, 
        headers = { HttpHeaders.CONTENT_TYPE, "X-Tenant-Id", HttpHeaders.AUTHORIZATION })
    public ResponseEntity<String> secRequestSingleDataInstanceProcessor(@RequestHeader(name = "Authorization", required = true) String authorization,
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
            @RequestHeader("Content-Length") long contentLength,
            @RequestBody(required = true) String body,
            HttpServletRequest request)
    {
        if (body.startsWith("[")) 
        {
            return secRequestMultipleDataInstancesProcessor(authorization, tenantId, contentLength, body, request);
        }

        long onStart = System.currentTimeMillis();

        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String responseBody = "";
        String action = "";
        int commandType = -1;

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            super.checkTenantIDAuthority(user, tenantId);

            JSONObject bodyRequest = null;
            try
            {
                bodyRequest = new JSONObject(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            action = bodyRequest.remove(Constant.action).toString();

            if (action.equalsIgnoreCase(Command.CREATE.name()))
            {
                commandType = Command.CREATE.ordinal();

                this.transactionService.create(xB3TraceId,
                        xB3SpanId,
                        xB3ParentSpanId,
                        tenantId, 
                        bodyRequest.getJSONArray("data"), 
                        new CredentialServiceImpl(this.transform(user)));

                return ResponseEntity.status(HttpStatus.CREATED).body(bodyRequest.getJSONArray("data").toString());
            }
            else if (action.equalsIgnoreCase(Command.UPDATE.name()))
            {
                commandType = Command.UPDATE.ordinal();

                this.transactionService.update(xB3TraceId,
                        xB3SpanId,
                        xB3ParentSpanId,
                        tenantId, 
                        bodyRequest.getJSONArray("data"), 
                        new CredentialServiceImpl(this.transform(user)));

                return ResponseEntity.ok().build();
            }
            else if (action.equalsIgnoreCase(Command.DELETE.name()))
            {
                commandType = Command.DELETE.ordinal();

                this.transactionService.delete(xB3TraceId,
                        xB3SpanId,
                        xB3ParentSpanId,
                        tenantId, 
                        bodyRequest.getJSONArray("data"), 
                        new CredentialServiceImpl(this.transform(user)));

                return ResponseEntity.ok().build();
            }
            else
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":unknow action."));
            }
        }
        catch (InterpreterControlledException e)
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).body(e.getMessage());
        }
        catch (Exception e)
        {
         // e.printStackTrace();

            JSONObject error;
            try
            {
                error = super.getErrorLogger().buildErrorMessage(tenantId, 
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
            }
            catch (Exception e2)
            {
                //e2.printStackTrace();

                error = new JSONObject();
                error.put("code", "417").put("message", e2.getMessage());
            }

            //logger.info(" > error: "+error);

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
                        commandType, 
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
                        commandType, 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }

    @PostMapping(path = "/no-ac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, 
        headers = { HttpHeaders.CONTENT_TYPE, "X-Tenant-Id", "X-Tenant-Secret" })
    public ResponseEntity<String> unsecRequestSingleDataInstanceProcessor(@RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret,
            @RequestHeader("Content-Length") long contentLength,
            @RequestBody(required = false) String body,
            HttpServletRequest request)
    {
        if (body.startsWith("["))
        {
            return unsecRequestMultipleDataInstanceProcessor(tenantId, tenantSecret, contentLength, body, request);
        }

        long onStart = System.currentTimeMillis();

        String responseBody = "";
        String action = "";
        int commandType = -1;

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            JSONObject bodyRequest = null;
            try
            {
                bodyRequest = new JSONObject(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            if (bodyRequest.has(Constant.action))
            {
                action = bodyRequest.remove(Constant.action).toString();
            } 
            else throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat("action undefined."));

            if (action.equalsIgnoreCase(Command.CREATE.name()))
            {
                commandType = Command.CREATE.ordinal();

                this.transactionService.create(xB3TraceId,
                        xB3SpanId,
                        xB3ParentSpanId,
                        tenantId, 
                        bodyRequest.getJSONArray("data"), 
                        new UserlessAuthorizationServiceImpl());

                return ResponseEntity.status(HttpStatus.CREATED).body(responseBody = bodyRequest.getJSONArray("data").toString());
            }
            else if (action.equalsIgnoreCase(Command.UPDATE.name()))
            {
                commandType = Command.UPDATE.ordinal();

                this.transactionService.update(xB3TraceId,
                        xB3SpanId,
                        xB3ParentSpanId,
                        tenantId, 
                        bodyRequest.getJSONArray("data"), 
                        new UserlessAuthorizationServiceImpl());

                return ResponseEntity.ok().build();
            }
            else if (action.equalsIgnoreCase(Command.DELETE.name()))
            {
                commandType = Command.DELETE.ordinal();

                this.transactionService.delete(xB3TraceId,
                        xB3SpanId,
                        xB3ParentSpanId,
                        tenantId, 
                        bodyRequest.getJSONArray("data"), 
                        new UserlessAuthorizationServiceImpl());

                return ResponseEntity.ok().build();
            }
            else throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":unknow action value."));
        }
        catch (InterpreterControlledException e)
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).body(e.getMessage());
        }
        catch (Exception e)
        {
            // e.printStackTrace();

            JSONObject error;
            try
            {
                error = super.getErrorLogger().buildErrorMessage(tenantId, 
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
            }
            catch (Exception e2)
            {
                //e2.printStackTrace();

                error = new JSONObject();
                error.put("code", "417").put("message", e2.getMessage());
            }

            //logger.info(" > error: "+error);

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
                        commandType, 
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
                        commandType, 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }
    
    private ResponseEntity<String> secRequestMultipleDataInstancesProcessor(@RequestHeader(name = "Authorization", required = true) String authorization,
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
            @RequestHeader("Content-Length") long contentLength,
            @RequestBody(required = true) String body,
            HttpServletRequest request)
    {
        long onStart = System.currentTimeMillis();

        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String responseBody = "";
        String action = "";
        int commandType = -1;

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            super.checkTenantIDAuthority(user, tenantId);

            /*
             * super.registerConsumption(tenantId, "dc/c/s/no-ac", body.length(), Consumption.Type.Request.ordinal(), _action, onStart, -1L);
             */

            JSONArray bodyRequest = null;
            try
            {
                bodyRequest = new JSONArray(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            //logger.info(" >>> body: "+bodyRequest.toString(2));

            JSONObject sqlStatements = new JSONObject();
            sqlStatements.put("create", new JSONObject());
            sqlStatements.put("createIdx", 0);
            sqlStatements.put("update", new JSONObject());
            sqlStatements.put("updateIdx", 0);
            sqlStatements.put("delete", new JSONObject());
            sqlStatements.put("deleteIdx", 0);

            for (int idx = 0; idx < bodyRequest.length(); idx++) 
            {
                JSONObject command = bodyRequest.getJSONObject(idx);
                action = command.remove(Constant.action).toString();
                if (action.equalsIgnoreCase(Command.CREATE.name()))
                {
                    commandType = Command.CREATE.ordinal();

                    //logger.info("to create: "+command.getJSONArray("data").toString());

                    JSONObject list = this.transactionService.getCreateStatement(tenantId, 
                            command.getJSONArray("data"), 
                            new CredentialServiceImpl(this.transform(user)));
                    list.keySet().forEach(key -> {
                        sqlStatements.getJSONObject("create").put(String.valueOf(sqlStatements.getInt("createIdx")), list.get(key));
                        sqlStatements.put("createIdx", sqlStatements.getInt("createIdx")+1);
                    });
                }
                else if (action.equalsIgnoreCase(Command.UPDATE.name()))
                {
                    commandType = Command.UPDATE.ordinal();

                    //logger.info("to update: "+command.getJSONArray("data").toString());

                    JSONObject list = this.transactionService.getUpdateStatements(xB3TraceId,
                            xB3SpanId,
                            xB3ParentSpanId,
                            tenantId, 
                            command.getJSONArray("data"), 
                            new CredentialServiceImpl(this.transform(user)));
                    list.keySet().forEach(key -> {
                        sqlStatements.getJSONObject("update").put(String.valueOf(sqlStatements.getInt("updateIdx")), list.get(key));
                        sqlStatements.put("updateIdx", sqlStatements.getInt("updateIdx")+1);
                    });
                }
                else if (action.equalsIgnoreCase(Command.DELETE.name()))
                {
                    commandType = Command.DELETE.ordinal();

                    //logger.info("to delete: "+command.getJSONArray("data").toString());

                    JSONObject list = this.transactionService.getDeleteStatements(tenantId, 
                            command.getJSONArray("data"), 
                            new CredentialServiceImpl(this.transform(user)));
                    list.keySet().forEach(key -> {
                        sqlStatements.getJSONObject("delete").put(String.valueOf(sqlStatements.getInt("deleteIdx")), list.get(key));
                        sqlStatements.put("deleteIdx", sqlStatements.getInt("deleteIdx")+1);
                    });
                }
                else
                {
                    throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":unknow action."));
                }
            }

            if (sqlStatements.getJSONObject("create").length() == 0) 
            {
                sqlStatements.remove("create");
            }

            if (sqlStatements.getJSONObject("update").length() == 0) 
            {
                sqlStatements.remove("update");
            }

            if (sqlStatements.getJSONObject("delete").length() == 0) 
            {
                sqlStatements.remove("delete");
            }

            //logger.info(" >>> sqlStatements: "+sqlStatements.toString(2));

            this.transactionService.executeSqlStatements(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, sqlStatements);

            return ResponseEntity.ok().build();
        }
        catch (InterpreterControlledException e)
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).body(e.getMessage());
        }
        catch (Exception e)
        {
         // e.printStackTrace();

            JSONObject error;
            try
            {
                error = super.getErrorLogger().buildErrorMessage(tenantId, 
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
            }
            catch (Exception e2)
            {
                //e2.printStackTrace();

                error = new JSONObject();
                error.put("code", "417").put("message", e2.getMessage());
            }

            //logger.info(" > error: "+error);

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
                        commandType, 
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
                        commandType, 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }

    public ResponseEntity<String> unsecRequestMultipleDataInstanceProcessor(@RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret,
            @RequestHeader("Content-Length") long contentLength,
            @RequestBody(required = false) String body,
            HttpServletRequest request)
    {
        long onStart = System.currentTimeMillis();

        String responseBody = "";
        String action = "";
        int commandType = -1;

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception(
                    String.valueOf(HttpStatus.UNAUTHORIZED.value()).concat(":this request is not authorized."));
            }

            /*
             * super.registerConsumption(tenantId, "dc/c/s/no-ac", body.length(), Consumption.Type.Request.ordinal(), _action, onStart, -1L);
             */

            JSONArray bodyRequest = null;
            try
            {
                bodyRequest = new JSONArray(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }

            //logger.info(" >>> body: "+bodyRequest.toString(2));

            JSONObject sqlStatements = new JSONObject();
            sqlStatements.put("create", new JSONObject());
            sqlStatements.put("createIdx", 0);
            sqlStatements.put("update", new JSONObject());
            sqlStatements.put("updateIdx", 0);
            sqlStatements.put("delete", new JSONObject());
            sqlStatements.put("deleteIdx", 0);

            for (int idx = 0; idx < bodyRequest.length(); idx++) 
            {
                JSONObject command = bodyRequest.getJSONObject(idx);
                action = command.remove(Constant.action).toString();
                if (action.equalsIgnoreCase(Command.CREATE.name()))
                {
                    commandType = Command.CREATE.ordinal();

                    //logger.info("to create: "+command.getJSONArray("data").toString());

                    JSONObject list = this.transactionService.getCreateStatement(tenantId, 
                            command.getJSONArray("data"), 
                            new UserlessAuthorizationServiceImpl());
                    list.keySet().forEach(key -> {
                        sqlStatements.getJSONObject("create").put(String.valueOf(sqlStatements.getInt("createIdx")), list.get(key));
                        sqlStatements.put("createIdx", sqlStatements.getInt("createIdx")+1);
                    });
                }
                else if (action.equalsIgnoreCase(Command.UPDATE.name()))
                {
                    commandType = Command.UPDATE.ordinal();

                    //logger.info("to update: "+command.getJSONArray("data").toString());

                    JSONObject list = this.transactionService.getUpdateStatements(xB3TraceId,
                            xB3SpanId,
                            xB3ParentSpanId,
                            tenantId, 
                            command.getJSONArray("data"), 
                            new UserlessAuthorizationServiceImpl());
                    list.keySet().forEach(key -> {
                        sqlStatements.getJSONObject("update").put(String.valueOf(sqlStatements.getInt("updateIdx")), list.get(key));
                        sqlStatements.put("updateIdx", sqlStatements.getInt("updateIdx")+1);
                    });
                }
                else if (action.equalsIgnoreCase(Command.DELETE.name()))
                {
                    commandType = Command.DELETE.ordinal();

                    //logger.info("to delete: "+command.getJSONArray("data").toString());

                    JSONObject list = this.transactionService.getDeleteStatements(tenantId, 
                            command.getJSONArray("data"), 
                            new UserlessAuthorizationServiceImpl());
                    list.keySet().forEach(key -> {
                        sqlStatements.getJSONObject("delete").put(String.valueOf(sqlStatements.getInt("deleteIdx")), list.get(key));
                        sqlStatements.put("deleteIdx", sqlStatements.getInt("deleteIdx")+1);
                    });
                }
                else
                {
                    throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":unknow action."));
                }
            }

            if (sqlStatements.getJSONObject("create").length() == 0) 
            {
                sqlStatements.remove("create");
            }

            if (sqlStatements.getJSONObject("update").length() == 0) 
            {
                sqlStatements.remove("update");
            }

            if (sqlStatements.getJSONObject("delete").length() == 0) 
            {
                sqlStatements.remove("delete");
            }

            //logger.info(" >>> sqlStatements: "+sqlStatements.toString(2));

            this.transactionService.executeSqlStatements(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, sqlStatements);

            return ResponseEntity.ok().build();
        }
        catch (InterpreterControlledException e)
        {
            return ResponseEntity.status(HttpStatus.CONTINUE).body(e.getMessage());
        }
        catch (Exception e)
        {
         // e.printStackTrace();

            JSONObject error;
            try
            {
                error = super.getErrorLogger().buildErrorMessage(tenantId, 
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
            }
            catch (Exception e2)
            {
                //e2.printStackTrace();

                error = new JSONObject();
                error.put("code", "417").put("message", e2.getMessage());
            }

            //logger.info(" > error: "+error);

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
                        commandType, 
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
                        commandType, 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }

    private com.yc.persistence.model.CUser transform(User user)
    {
        final Set<com.yc.loco3.security.Role> roles = new HashSet<com.yc.loco3.security.Role>();
        user.getRoles().forEach(role -> roles.add(new com.yc.persistence.model.CRole(role.getName())));
        return new com.yc.persistence.model.CUser(user.getUsername(), roles);
    }
}
