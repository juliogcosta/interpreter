package com.yc.persistence.api;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.yc.doc.endpoint.Endpoint;
import com.yc.doc.endpoint.EndpointCode;
import com.yc.doc.endpoint.EndpointRequest;
import com.yc.doc.endpoint.EndpointResponse;
import com.yc.error.utils.ErrorMessageSpec;
import com.yc.models.nosql.Consumption;
import com.yc.models.sql.up.Role;
import com.yc.persistence.exception.InterpreterControlledException;
import com.yc.persistence.service.QueryServiceImpl;
import com.yc.pr.models.User;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class STransactionController extends Controller
{
    @Autowired
    protected QueryServiceImpl queryService;

    @Value("${yc.security.keys.accessToken.secret}") 
    String accessTokenSecret;

    @Endpoint(
        name = "(UP/UA) Executar Consulta | com cabeçalho Authorization.", 
        description = "Esse endpoint deve ser usado com o fim de recuperar dados persistidos para um "
            + "schema/tenant-id. Sendo o caso em que o controle de acesso ao endpoint é feito com "
            + "base no cabeçalho Authorization.", 
        authorizedRoles = { Role.PUBLIC }, 
        bodyRequest = @EndpointRequest(
            format = "[{\"entityName\":{\"attributeName\":\"attributeValue\"}}]", 
            description = "O formato é conforme definido em format. Um conjunto de consultas podem ser "
                + "realizadas em uma única requisição. Caso deseje uma só consulta, é possível enviar "
                + "apenas uma consulta fora do array apresentado no format. A bem do uso de filtros, "
                + "valore os atributos conforme os valores que deseja como filtro para a consulta no "
                + "banco de dados. Para mais detalhes a respeito do uso da linguagem de consulta, "
                + "consultar documentação."), 
        bodyResponse = @EndpointResponse(
            formatOnSuccess = "--", 
            formatOnError = "{"
                + "\"code\":\"\", " + "\"message\":\"\", " + "\"alias\":\"\", " + "\"service\":\"\" "
                + "}", 
            description = "Em caso de sucesso, uma mensagem de retorno é enviada no corpo da resposta. "
                + "Quanto a situação em que ocorre erro na execução da requisição, "
                + "qualquer que seja o erro, o formato de resposta será sempre o mesmo."), 
        successStatusCode = {
            "200:Registro(s) Localizado(m)",
            ErrorMessageSpec.NoContent.Code.Value + ":" + ErrorMessageSpec.NoContent.Message.Pt_Br
        }, 
        errorStatusCode = { 
            ErrorMessageSpec.BadRequest.Code.Value + ":" + ErrorMessageSpec.BadRequest.Message.Pt_Br,
            ErrorMessageSpec.Unauthorized.Code.Value + ":" + ErrorMessageSpec.Unauthorized.Message.Pt_Br,
            ErrorMessageSpec.ForbiddenAccess.Code.Value + ":" + ErrorMessageSpec.ForbiddenAccess.Message.Pt_Br,
            ErrorMessageSpec.UnexpectedFailed.Code.Value + ":" + ErrorMessageSpec.UnexpectedFailed.Message.Pt_Br,
            ErrorMessageSpec.InternalServerError.Code.Value + ":" + ErrorMessageSpec.InternalServerError.Message.Pt_Br
        }, 
        ignore = false, 
        code = @EndpointCode())
    @PostMapping(path = "/s/ac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {
        HttpHeaders.CONTENT_TYPE, "X-Tenant-Id", HttpHeaders.AUTHORIZATION
    })
    public ResponseEntity<String> request(@RequestHeader(name = "Authorization", required = true) String authorization,
        @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
        @RequestHeader(name = "Content-Length") long contentLength, @RequestBody(required = true) String body,
        HttpServletRequest request)
    {
        //logger.info(" > body: "+body);
        
        if (body.startsWith("{"))
        {
            return closeAltRequest(authorization, tenantId, contentLength, body, request);
        }

        long onStart = System.currentTimeMillis();

        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String responseBody = "";

        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");
        
        try
        {
            super.checkTenantIDAuthority(user, tenantId);

            JSONArray bodyRequest = null;
            try
            {
                bodyRequest = new JSONArray(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }
            
            JSONArray resp = this.queryService.readByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, bodyRequest, this.transform(user), true);
            int idx = 0;
            for (; idx < resp.length(); idx++)
            {
                JSONObject item = resp.getJSONObject(idx);
                if (item.getJSONArray(item.keys().next()).length() > 0)
                {
                    break;
                }
            }
            
            //logger.info(" > resp: "+resp);
            
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
    
    @Endpoint(name = "(UP/UA) Executar Consulta | com cabeçalho Tenant-Secret.", description = "Esse endpoint deve ser usado com o fim de recuperar dados persistidos para um "
        + "schema/tenant-id. Sendo o caso em que o controle de acesso ao endpoint é feito com "
        + "base no cabeçalho X-Tenant-Secret.", method = "POST", authorizedRoles = { Role.PUBLIC
    }, bodyRequest = @EndpointRequest(format = "[{\"entityName\":{\"attributeName\":\"attributeValue\"}}]", description = "O formato é conforme definido em format. Um conjunto de consultas podem ser "
        + "realizadas em uma única requisição. Caso deseje uma só consulta, é possível enviar "
        + "apenas uma consulta fora do array apresentado no format. A bem do uso de filtros, "
        + "valore os atributos conforme os valores que deseja como filtro para a consulta no "
        + "banco de dados. Para mais detalhes a respeito do uso da linguagem de consulta, "
        + "consultar documentação."), bodyResponse = @EndpointResponse(formatOnSuccess = "--", formatOnError = "{"
            + "\"code\":\"\", " + "\"message\":\"\", " + "\"alias\":\"\", " + "\"service\":\"\" "
            + "}", description = "Em caso de sucesso, uma mensagem de retorno é enviada no corpo da resposta. "
                + "Quanto a situação em que ocorre erro na execução da requisição, "
                + "qualquer que seja o erro, o formato de resposta será sempre o mesmo."), successStatusCode = {
                    "200:Registro(s) Localizado(m)",
                    ErrorMessageSpec.NoContent.Code.Value + ":" + ErrorMessageSpec.NoContent.Message.Pt_Br
    }, errorStatusCode = { ErrorMessageSpec.BadRequest.Code.Value + ":" + ErrorMessageSpec.BadRequest.Message.Pt_Br,
        ErrorMessageSpec.Unauthorized.Code.Value + ":" + ErrorMessageSpec.Unauthorized.Message.Pt_Br,
        ErrorMessageSpec.ForbiddenAccess.Code.Value + ":" + ErrorMessageSpec.ForbiddenAccess.Message.Pt_Br,
        ErrorMessageSpec.UnexpectedFailed.Code.Value + ":" + ErrorMessageSpec.UnexpectedFailed.Message.Pt_Br,
        ErrorMessageSpec.InternalServerError.Code.Value + ":" + ErrorMessageSpec.InternalServerError.Message.Pt_Br
    }, ignore = false, code = @EndpointCode())
    @PostMapping(path = "/s/no-ac", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {
        HttpHeaders.CONTENT_TYPE, "X-Tenant-Id", "X-Tenant-Secret"
    })
    public ResponseEntity<String> openRequest(@RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
        @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret,
        @RequestHeader(name = "Content-Length") long contentLength, @RequestBody(required = true) String body,
        HttpServletRequest request)
    {
        if (body.startsWith("{"))
        {
            return openAltRequest(tenantId, tenantSecret, contentLength, body, request);
        }
        
        long onStart = System.currentTimeMillis();
        
        String responseBody = "";
        
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");
        
        try
        {
            JSONArray datas = null;
            try
            {
                datas = new JSONArray(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }
            
            JSONArray resp = this.queryService.readByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, datas, null, true);

            int idx = 0;
            for (; idx < resp.length(); idx++)
            {
                JSONObject item = resp.getJSONObject(idx);
                if (item.getJSONArray(item.keys().next()).length() > 0)
                {
                    break;
                }
            }
            
            if (idx < resp.length())
            {
                return ResponseEntity.ok(responseBody = resp.toString());
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
            String endpoint = "";
            String method = "POST";
            if (request != null)
            {
                endpoint = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
                method = request.getMethod();
            }
            
            //logger.info(" > "+e.getMessage());
            //e.printStackTrace();

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
                    endpoint, 
                    method,  
                    null, 
                    null, 
                    "i",
                    false);

            return ResponseEntity.status(error.getInt("code")).body(error.toString());
        }
        finally
        {
            String endpoint = "";
            if (request != null)
            {
                endpoint = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
            }
            
            if (responseBody == null)
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(endpoint), 
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
                        this.appName.concat(endpoint),
                        responseBody.length() + contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }
    
    public ResponseEntity<String> openAltRequest(
        @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
        @RequestHeader(name = "X-Tenant-Secret", required = true) String tenantSecret,
        @RequestHeader(name = "Content-Length") long contentLength, 
        @RequestBody(required = true) String body,
        HttpServletRequest request)
    {
        long onStart = System.currentTimeMillis();
        
        String responseBody = "";
        
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");
        
        try
        {
            JSONObject data = null;
            try
            {
                data = new JSONObject(body);
            }
            catch (Exception e)
            {
                throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST.value()).concat(":").concat(e.getMessage()));
            }
            
            JSONArray resp = this.queryService.readAltByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, data, null, true);
            
            if (resp.length() > 0)
            {
                return ResponseEntity.ok(responseBody = resp.toString());
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
            String endpoint = "";
            String method = "POST";
            if (request != null)
            {
                endpoint = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
                method = request.getMethod();
            }

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
                    endpoint, 
                    method, 
                    null, 
                    null, 
                    "i",
                    false);

            return ResponseEntity.status(error.getInt("code")).body(error.toString());
        }
        finally
        {
            String endpoint = "";
            if (request != null)
            {
                endpoint = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
            }
            
            if (responseBody == null)
            {
                super.getLogCollector().registerApplicationConsumption(tenantId, 
                        tenantId, 
                        this.appName.concat(endpoint), 
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
                        this.appName.concat(endpoint),
                        responseBody.length() + contentLength, 
                        Consumption.Type.Response.ordinal(), 
                        Command.READ.ordinal(), 
                        onStart, 
                        System.currentTimeMillis() - onStart);
            }
        }
    }
    
    private ResponseEntity<String> closeAltRequest(
        @RequestHeader(name = "Authorization", required = true) String authorization,
        @RequestHeader(name = "X-Tenant-Id", required = true) String tenantId,
        @RequestHeader(name = "Content-Length") long contentLength, @RequestBody(required = true) String body,
        HttpServletRequest request)
    {
        long onStart = System.currentTimeMillis();
        
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        String responseBody = "";
        
        final String xB3TraceId = request.getHeader("x-b3-traceid");
        final String xB3SpanId = request.getHeader("x-b3-spanid");
        final String xB3ParentSpanId = request.getHeader("x-b3-parentspanid");
        
        try
        {
            //logger.info(" > authorization: "+authorization);

            /*
            final JwtUtils jwtUtils = new JwtUtils();
            jwtUtils.setup(this.accessTokenSecret);
            jwtUtils.getUserTenantIdsFromAccessToken(authorization).forEach(id -> {
                logger.info(" > tId: "+id);
            });
             */
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
            
            JSONArray resp = this.queryService.readAltByCriteria(xB3TraceId, xB3SpanId, xB3ParentSpanId, tenantId, bodyRequest, this.transform(user), true);

            //logger.info(" > resp: "+resp);

            if (0 < resp.length())
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

    private com.yc.persistence.model.CUser transform(User user)
    {
        final Set<br.edu.ufrn.loco3.security.Role> roles = new HashSet<br.edu.ufrn.loco3.security.Role>();
        user.getRoles().forEach(role -> roles.add(new com.yc.persistence.model.CRole(role.getName())));
        return new com.yc.persistence.model.CUser(user.getUsername(), roles);
    }
}
