package com.yc.utils;

import java.util.Map;

public interface ContextManagerControl
{
    public void initKey(String key, Long expires, Boolean force) throws Exception;

    public Boolean containsKey(String key);

    public Map<String, Object> removeKey(String key);

    public Boolean getLogConsole(String key);

    void setLogConsole(String key, Boolean logConsole);

    public Boolean getSaveTrace(String key);

    public void setSaveTrace(String key, Boolean saveTrace);

    public Boolean getSaveData(String key);

    public void setSaveData(String key, Boolean saveData, Long expires);

    public Boolean getSaveLog(String key);

    public void setSaveLog(String key, Boolean saveLog);

    public Object get(String key, String objectKey);

    public void set(String key, String objectKey, Object objectValue);
}
