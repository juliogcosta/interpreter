package com.yc.utils.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yc.utils.ContextManagerControl;

@Service
public class ContextManagerControlImpl implements ContextManagerControl
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    static final public String logConsole = "logConsole";
    static final public String saveTrace = "saveTrace";
    static final public String saveData = "saveData";
    static final public String saveLog = "saveLog";

    final private Map<String, Map<String, Object>> logMap = new HashMap<>();

    @Override
    public void initKey(String key, Long expires, Boolean force) throws Exception
    {
        if (this.containsKey(key))
        {
            if (force)
            {
                this.setLogConsole(key, false);
                this.setSaveTrace(key, false);
                this.setSaveData(key, false, expires);
                this.setSaveLog(key, false);
            }
        }
        else
        {
            this.logMap.put(key, new HashMap<>());

            this.setLogConsole(key, false);
            this.setSaveTrace(key, false);
            this.setSaveData(key, false, expires);
            this.setSaveLog(key, false);
        }
    }

    @Override
    public Boolean containsKey(String key)
    {
        return this.logMap.containsKey(key);
    }

    @Override
    public Map<String, Object> removeKey(String key)
    {
        return this.logMap.remove(key);
    }

    @Override
    public Boolean getLogConsole(String key)
    {
        return this.logMap.get(key).get(ContextManagerControlImpl.logConsole).toString().equals("true");
    }

    @Override
    public void setLogConsole(String key, Boolean logConsole)
    {
        this.logMap.get(key).put(ContextManagerControlImpl.logConsole, logConsole);
    }

    @Override
    public Boolean getSaveTrace(String key)
    {
        return this.logMap.get(key).get(ContextManagerControlImpl.saveTrace).toString().equals("true");
    }

    @Override
    public void setSaveTrace(String key, Boolean saveTrace)
    {
        this.logMap.get(key).put(ContextManagerControlImpl.saveTrace, saveTrace);
    }

    @Override
    public Boolean getSaveData(String key)
    {
        return this.logMap.get(key).get(ContextManagerControlImpl.saveData).toString().equals("true");
    }

    @Override
    public void setSaveData(String key, Boolean saveData, Long expires)
    {
        if (saveData)
        {
            new Timer().schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    ContextManagerControlImpl.this.logMap.get(key).put(ContextManagerControlImpl.saveData, false);

                    cancel();
                }
            }, expires);
        }

        this.logMap.get(key).put(ContextManagerControlImpl.saveData, saveData);
    }

    @Override
    public Boolean getSaveLog(String key)
    {
        return this.logMap.get(key).get(ContextManagerControlImpl.saveLog).toString().equals("true");
    }

    @Override
    public void setSaveLog(String key, Boolean saveLog)
    {
        this.logMap.get(key).put(ContextManagerControlImpl.saveLog, saveLog);
    }

    @Override
    public Object get(String key, String objectKey)
    {
        return this.logMap.get(key).get(objectKey);
    }

    @Override
    public void set(String key, String objectKey, Object objectValue)
    {
        this.logMap.get(key).put(objectKey, objectValue);
    }
}
