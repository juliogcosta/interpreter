package com.yc.error.handler;

import java.sql.Timestamp;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.yc.error.utils.ErrorMessageSpec;

@Component
public class ErrorHandlerImpl implements ErrorHandler
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public JSONObject buildResponseEntityError(Exception e, 
            String tenantId, 
            String traceId, 
            String spanId, 
            String parentSpanId, 
            String orgName, 
            String projectName, 
            String username,  
            String service, 
            String httpEndpoint, 
            String httpMethod, 
            String elementUuid, 
            String body, 
            Boolean saveTrace, 
            Boolean saveBody) 
    {
        //logger.info(" % > tenantId: "+tenantId);

        final JSONObject jsError = this.getError(e);
        //logger.info(" % > jsError: "+jsError);

        final JSONObject error = new JSONObject();
        error.put("message", jsError.getString("message"));
        error.put("customMessage", jsError.getString("c-message"));
        error.put("datetime", System.currentTimeMillis());
        error.put("timestamp", new Timestamp(System.currentTimeMillis()).toString());
        error.put("tenantId", tenantId);
        error.put("id", traceId);
        error.put("spanId", spanId);
        error.put("parentSpanId", parentSpanId);
        error.put("orgName", orgName);
        error.put("projectName", projectName);
        error.put("username", username);
        error.put("service", service);
        error.put("httpEndpoint", httpEndpoint);
        error.put("httpMethod", httpMethod);
        error.put("code", jsError.getString("statusCode"));
        //logger.info(" % > error: "+error);

        if (saveBody) 
        {
            error.put("httpBody", saveBody);
        }

        if (saveTrace) 
        {
            StringBuffer buffer = new StringBuffer();
            for (StackTraceElement trace : e.getStackTrace())
            {
                buffer.append(trace.getClassName().concat(".").concat(trace.getMethodName()).concat("[").concat(String.valueOf(trace.getLineNumber())).concat("]\n"));
            }
            error.put("trace", buffer.toString());
        }
        //logger.info(" % > 0");

        if (elementUuid != null && !elementUuid.trim().equals("")) 
        {
            error.put("elementUuid", elementUuid);
        }

        //logger.info(" % > 1");

        return error;
    }

    @Override
    public JSONObject getError(Exception e)
    {
        JSONObject jsObject = new JSONObject();

        if (e.getMessage() == null)
        {
            if (e.getCause() == null) 
            {
                jsObject.put("statusCode", ErrorMessageSpec.InternalServerError.Code.Value);
                Exception ne = new Exception("Falha detectada, mas não reconhecida.");
                jsObject.put("exception", ne);
                jsObject.put("message", ne.getMessage());
                jsObject.put("c-message", ErrorMessageSpec.InternalServerError.Message.Pt_Br);
            }
            else if (e.getCause().getMessage() == null) 
            {
                jsObject.put("statusCode", ErrorMessageSpec.InternalServerError.Code.Value);
                Exception ne = new Exception("Falha detectada, mas não reconhecida.");
                jsObject.put("exception", ne);
                jsObject.put("message", ne.getMessage());
                jsObject.put("c-message", ErrorMessageSpec.InternalServerError.Message.Pt_Br);
            }
            else
            {
                jsObject.put("statusCode", ErrorMessageSpec.InternalServerError.Code.Value);
                Exception ne = new Exception(e.getCause());
                jsObject.put("exception", ne);
                jsObject.put("message", ne.getMessage());
                jsObject.put("c-message", ErrorMessageSpec.InternalServerError.Message.Pt_Br);
            }
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.NoContent.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.NoContent.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.NoContent.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.BadRequest.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.BadRequest.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.BadRequest.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.Unauthorized.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.Unauthorized.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.Unauthorized.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.ForbiddenAccess.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.ForbiddenAccess.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.ForbiddenAccess.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.NotFound.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.NotFound.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.NotFound.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.Conflict.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.Conflict.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.Conflict.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.PayloadTooLarge.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.PayloadTooLarge.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.PayloadTooLarge.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.InternalServerError.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.InternalServerError.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.InternalServerError.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith(ErrorMessageSpec.UnexpectedFailed.Code.Value.concat(":")))
        {
            jsObject.put("statusCode", ErrorMessageSpec.UnexpectedFailed.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.InternalServerError.Message.Pt_Br);
        }
        else if (e.getMessage().contains("23505"))
        {
            jsObject.put("statusCode", ErrorMessageSpec.Conflict.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.Conflict.Message.Pt_Br);
        }
        else if (e.getMessage().startsWith("40901:"))
        {
            jsObject.put("statusCode", 40901);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", e.getMessage().replace("40901", ""));
        }
        else
        {
            jsObject.put("statusCode", ErrorMessageSpec.UnexpectedFailed.Code.Value);
            jsObject.put("exception", e);
            jsObject.put("message", e.getMessage());
            jsObject.put("c-message", ErrorMessageSpec.InternalServerError.Message.Pt_Br);
        }

        return jsObject;
    }
}
