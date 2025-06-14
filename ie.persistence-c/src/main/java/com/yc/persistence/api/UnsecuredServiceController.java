package com.yc.persistence.api;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.yc.utils.ContextManagerControl;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping(UnsecuredServiceController.urlPrefix)
@RestController
public class UnsecuredServiceController extends Controller
{
    static final public String urlPrefix = "/ot23r0amn4za5/unsecured";

    @Autowired
    private ContextManagerControl contextManagerControl;

    @PutMapping(path = "/save-trace/enable/{enable}")
    public ResponseEntity<String> setSaveTrace(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            @PathVariable(required = true) Boolean enable, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            this.contextManagerControl.setSaveTrace(tenantId, enable);

            JSONObject response = new JSONObject();
            response.put("saveTrace", this.contextManagerControl.getSaveTrace(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    @GetMapping(path = "/save-trace")
    public ResponseEntity<String> getSaveTrace(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("saveTrace", this.contextManagerControl.getSaveTrace(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    @PutMapping(path = "/save-data/enable/{enable}/expires/{expires}")
    public ResponseEntity<String> setSaveData(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            @PathVariable(required = true) Boolean enable, 
            @PathVariable(required = true) Long expires, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            this.contextManagerControl.setSaveData(tenantId, enable, expires);

            JSONObject response = new JSONObject();
            response.put("saveData", this.contextManagerControl.getSaveData(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    @GetMapping(path = "/save-data")
    public ResponseEntity<String> getSaveData(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("saveData", this.contextManagerControl.getSaveData(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    @PutMapping(path = "/save-log/enable/{enable}")
    public ResponseEntity<String> setSaveLog(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            @PathVariable(required = true) Boolean enable, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            this.contextManagerControl.setSaveLog(tenantId, enable);

            JSONObject response = new JSONObject();
            response.put("saveLog", this.contextManagerControl.getSaveLog(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    @GetMapping(path = "/save-log")
    public ResponseEntity<String> getSaveLog(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("saveLog", this.contextManagerControl.getSaveLog(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    @PutMapping(path = "/log-console/enable/{enable}")
    public ResponseEntity<String> setLogConsole(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            @PathVariable(required = true) Boolean enable, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            this.contextManagerControl.setLogConsole(tenantId, enable);

            JSONObject response = new JSONObject();
            response.put("logConsole", this.contextManagerControl.getLogConsole(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    @GetMapping(path = "/log-console")
    public ResponseEntity<String> getLogConsole(
            @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId, 
            @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret, 
            HttpServletRequest request)
    {
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");

        try
        {
            if (!this.contextManagerControl.containsKey(tenantId)) 
            {
                this.contextManagerControl.initKey(tenantId, super.ttl, false);
                this.contextManagerControl.setLogConsole(tenantId, super.logDisplay);
            }

            //logger.info(" > x-tenant-secret: "+tenantSecret);
            JSONObject cache = this.schemaCacheService.read(xB3TraceId, xB3SpanId, xB3ParentSpanId, "persistence-service-".concat(tenantSecret));
            //logger.info(" > cache: "+cache.toString());
            if (!(cache.getBoolean("status")
                && cache.getJSONObject("content").toString().equals(tenantId)))
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("logConsole", this.contextManagerControl.getLogConsole(tenantId));

            return ResponseEntity.ok().body(response.toString());
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
    }

    /*@PutMapping(path = "/save-trace/enable/{enable}/{pmsk}")
    public ResponseEntity<String> putSaveTrace(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
             
            @PathVariable(name = "enable", required = true) Boolean enable, 
            @PathVariable(name = "pmsk", required = true) String pmsk, 
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            super.schemaCacheWrapper.setSaveTrace(tenantId, enable);

            JSONObject response = new JSONObject();
            response.put("saveTrace", super.schemaCacheWrapper.isToSaveTrace(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/save-trace/enable/{pmsk}")
    public ResponseEntity<String> getSaveTrace(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("saveTrace", super.schemaCacheWrapper.isToSaveTrace(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @PutMapping(path = "/log-console/enable/{enable}/{pmsk}")
    public ResponseEntity<String> putLogConsole(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
            
            @PathVariable(name = "enable", required = true) Boolean enable, 
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            super.schemaCacheWrapper.setLogConsole(tenantId, enable);

            JSONObject response = new JSONObject();
            response.put("logConsole", super.schemaCacheWrapper.isToLogConsole(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null,
                    null,
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/log-console/enable/{pmsk}")
    public ResponseEntity<String> getLogConsole(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("logConsole", super.schemaCacheWrapper.isToLogConsole(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null,
                    null,
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @PutMapping(path = "/save-data/enable/{enable}/{pmsk}")
    public ResponseEntity<String> putSaveData(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
            
            @PathVariable(name = "enable", required = true) Boolean enable, 
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            super.schemaCacheWrapper.setSaveData(tenantId, enable);

            JSONObject response = new JSONObject();
            response.put("saveData", super.schemaCacheWrapper.isToSaveData(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null,
                    null,
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/save-data/enable/{pmsk}")
    public ResponseEntity<String> getSaveData(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("saveData", super.schemaCacheWrapper.isToSaveData(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null,
                    null,
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @PutMapping(path = "/save-log/enable/{enable}/{pmsk}")
    public ResponseEntity<String> putSaveLog(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
            
            @PathVariable(name = "enable", required = true) Boolean enable, 
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            super.schemaCacheWrapper.setSaveLog(tenantId, enable);

            JSONObject response = new JSONObject();
            response.put("saveLog", super.schemaCacheWrapper.isToSaveLog(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null,
                    null,
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/save-log/enable/{pmsk}")
    public ResponseEntity<String> getSaveLog(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("saveLog", super.schemaCacheWrapper.isToSaveLog(tenantId));

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null,
                    null,
                    this.appName, 
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @PutMapping(path = "/monitor-not-fount-error/enable/{enable}/{pmsk}")
    public ResponseEntity<String> notFoundErrorState(
            
            @PathVariable(name = "enable", required = true) Boolean enable, 
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            Stats.monitorNotFoundError = enable;

            JSONObject response = new JSONObject();
            response.put("notFoundErrorStats", Stats.monitorNotFoundError);

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    apiTenantIdAdmin, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/monitor-not-fount-error/enable/{pmsk}")
    public ResponseEntity<String> getNotFoundErrorState(
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            response.put("notFoundErrorStats", Stats.monitorNotFoundError);

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    apiTenantIdAdmin, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/monitor-not-fount-error/{pmsk}")
    public ResponseEntity<String> getNotFoundErrorMap(
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            JSONObject response = new JSONObject();
            Stats.monitorNotFoundErrorMap.keySet().forEach(origin -> {
                response.put(origin, Stats.monitorNotFoundErrorMap.get(origin));
            });

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    apiTenantIdAdmin, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @DeleteMapping(path = "/monitor-not-fount-error/{pmsk}")
    public ResponseEntity<String> deleteNotFoundErrorMap(
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            Stats.monitorNotFoundErrorMap.clear();

            return ResponseEntity.ok("{}");
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    apiTenantIdAdmin, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/monitor-not-fount-error/origin/{origin}/{pmsk}")
    public ResponseEntity<String> getNotFoundErrorMapByOrigin(
             
            @PathVariable(name = "origin", required = true) String origin, 
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            JSONArray response = new JSONArray();
            if (Stats.monitorNotFoundErrorMap.containsKey(origin)) 
            {
                response = Stats.monitorNotFoundErrorMap.get(origin);
            }

            return ResponseEntity.ok().body(response.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    apiTenantIdAdmin, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @GetMapping(path = "/tenantIds-for-cached-schemas/{pmsk}")
    public ResponseEntity<String> getTenantIdsForCachedSchemas(
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            return ResponseEntity.ok(super.schemaCacheWrapper.getTenantIdsForCachedSchemas().toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    apiTenantIdAdmin, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }

    @DeleteMapping(path = "/tenantId-for-cached-schema/tenant-id/{tenantId}/{pmsk}")
    public ResponseEntity<String> deleteTenantIdFromCachedSchemas(
            @RequestHeader(name = "tenantId", required = true) String tenantId, 
             
            @PathVariable(name = "pmsk", required = true) String pmsk,
            HttpServletRequest request)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            super.schemaCacheWrapper.dropSchema(tenantId);

            return ResponseEntity.ok("{}");
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, 
                    tenantId, 
                    id, 
                    null, 
                    null, 
                    this.this.appName,  
                    request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString()
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), 
                    request.getMethod(),
                    "");
        }
    }*/

    /*
    @GetMapping(path = "/monitor-not-fount-error/origin/{origin}/{pmsk}")
    public ResponseEntity<String> getErrorStatsForOrigin(
            
            @PathVariable(name = "origin", required = true) String origin, 
            @PathVariable(name = "pmsk", required = true) String pmsk)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            return ResponseEntity.ok(Stats.monitorNotFoundErrorMap.get(origin).toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, "ycapimanager", id, 
                    "persistenceqdc/.../monitor-not-fount-error/origin/{".concat(origin).concat("}/...")
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), request.getMethod());
        }
    }
     */

    /*
    @GetMapping(path = "/monitor-not-fount-error/origin/{pmsk}")
    public ResponseEntity<String> getErrorStats(
             
            @PathVariable(name = "pmsk", required = true) String pmsk)
    {
        try
        {
            if (!pmsk.equals(PersistenceApplication.platformManagerSecretKey)) 
            {
                throw new Exception("403:request forbidden.");
            }

            return ResponseEntity.ok(Stats.monitorNotFoundErrorMap.toString());
        }
        catch (Exception e)
        {
            return super.buildResponseEntityError(e, "ycapimanager", id, 
                    "persistenceqdc/.../monitor-not-fount-error/origin/..."
                            .replace(UnsecuredServiceController.urlPrefix.substring(1), "..."), request.getMethod());
        }
    }
     */
}