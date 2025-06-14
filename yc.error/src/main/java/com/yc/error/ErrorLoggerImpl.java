package com.yc.error;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yc.error.dispatcher.ErrorExchangeSender;
import com.yc.error.handler.ErrorHandler;
import com.yc.utils.ContextManagerControl;

@Component
public class ErrorLoggerImpl implements ErrorLogger
{
    final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    public ErrorExchangeSender exchangeSender;

    @Autowired
    public ErrorHandler errorHandler;

    @Autowired
    public ContextManagerControl contextManagerControl;

    @Override
    public JSONObject buildErrorMessage(String contextManagerControlKey, 
        Exception e, 
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
        String httpBody, 
        String queueTarget,
        Boolean full)
    {
        //logger.info(" # > buildErrorMessage(): in!");

        //logger.info(" # > buildErrorMessage(): contextManagerControlKey: "+contextManagerControlKey);

         Boolean contextManagerControlContainsKey = contextManagerControlKey == null ? false : this.contextManagerControl.containsKey(contextManagerControlKey);

         //logger.info(" # > contextManagerControlContainsKey: "+contextManagerControlContainsKey);

        if (contextManagerControlContainsKey)
        {
            //logger.info(" # > this.contextManagerControl.getLogConsole("+contextManagerControlKey+"): "+this.contextManagerControl.getLogConsole(contextManagerControlKey));

            if (this.contextManagerControl.getLogConsole(contextManagerControlKey))
            {
                //logger.info(" # > !!!!!!!!!!!");

                e.printStackTrace();
            } 
            //else logger.info(" > contextManagerControl.getLogConsole("+contextManagerControlKey+"): "+this.contextManagerControl.getLogConsole(contextManagerControlKey));
        }
        //else logger.info(" > contextManagerControl contains key?: "+contextManagerControlContainsKey);

        //logger.info(" # > e.getMessage(): "+e.getMessage());

        final JSONObject jsError = this.errorHandler.buildResponseEntityError(e, 
                tenantId, 
                traceId,
                spanId, 
                parentSpanId, 
                orgName, 
                projectName, 
                username, 
                service,
                httpEndpoint, 
                httpMethod, 
                elementUuid, 
                httpBody,
                contextManagerControlContainsKey ? this.contextManagerControl.getSaveTrace(contextManagerControlKey) : true,
                contextManagerControlContainsKey ? this.contextManagerControl.getSaveData(contextManagerControlKey) : false);


        //logger.info(" # > contextManagerControlContainsKey: "+contextManagerControlContainsKey);

        if (contextManagerControlContainsKey = true)
        {
            //logger.info(" # > this.contextManagerControl.getSaveLog("+contextManagerControlKey+"): "+this.contextManagerControl.getSaveLog(contextManagerControlKey));

            if (this.contextManagerControl.getSaveLog(contextManagerControlKey))
            {
                //logger.info(" # > queueTarget: "+queueTarget);

                if (queueTarget.equals("i"))
                {
                    //logger.info(" # > jsError[i]: "+jsError);

                    this.exchangeSender.registerPlatformError(jsError.toString());;
                }
                else if (queueTarget.equals("e"))
                {
                    //logger.info(" # > jsError[e]: "+jsError);

                    this.exchangeSender.registerApplicationError(jsError.toString());
                }
                else
                {
                    //logger.info(" # > jsError[]: "+jsError);

                    new Exception("queueTarget unknown: ".concat(queueTarget)).printStackTrace();
                }
            }
        }

        if (full)
        {
            jsError.remove("tenantId");
            jsError.remove("datetime");
            jsError.remove("id");
            jsError.remove("spanId");
            jsError.remove("parentSpanId");
            jsError.remove("orgName");
            jsError.remove("projectName");
            jsError.remove("username");
            jsError.remove("httpEndpoint");
            jsError.remove("httpMethod");
            jsError.remove("elementUuid");
            jsError.remove("trace");
        }

        jsError.put("code", jsError.remove("code"));
        jsError.put("alias", jsError.remove("customMessage"));

        //logger.info(" # > jsError: "+jsError);
        
        return jsError;
    }
}
