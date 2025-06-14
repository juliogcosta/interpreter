package com.yc.utils.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LocalCacheImpl
{
    final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    final static private String TTL = "TTL";
    final static private String RESOURCE = "RESOURCE";
    final static private String EXPIRES_AT = "EXPIRES_AT";
    final static private String REVERSE_KEYs = "REVERSE_KEYs";

    final private Map<String, Map<String, Object>> localCache = new HashMap<>();
    final private Map<String, String> localCacheReverseKey = new HashMap<>();

    public void save(String key, JSONArray reverseKeys, Object resource, JSONObject subMap, Long ttl)
    {
        logger.info(" > reverseKeys: "+reverseKeys);

        reverseKeys.forEach(reverseKey -> {
            this.localCacheReverseKey.put(reverseKey.toString(), key);
        });

        //logger.info(" > localCacheReverseKey: "+localCacheReverseKey);

        this.localCache.put(key, new HashMap<>());

        subMap.put(TTL, ttl);
        subMap.put(REVERSE_KEYs, reverseKeys);
        subMap.put(RESOURCE, resource);
        subMap.put(EXPIRES_AT, System.currentTimeMillis()+ttl);
        subMap.keySet().forEach(subKey -> {
            LocalCacheImpl.this.localCache.get(key).put(subKey, subMap.get(subKey));
        });
    }

    public Boolean containsKey(String key)
    {
        if (this.localCache.containsKey(key))
        {
            this.localCache.get(key).put(EXPIRES_AT, 
                    System.currentTimeMillis() + (Long) this.localCache.get(key).get(TTL));
            return true;
        } else return false;
    }

    public Object getResource(String key)
    {
        if (this.containsKey(key))
        {
            return (JSONObject) this.localCache.get(key).get(RESOURCE);
        } else return null;
    }

    public Boolean containsReverseKey(String reverseKey)
    {
        //logger.info(" > localCacheReverseKey: "+localCacheReverseKey);

        if (this.localCacheReverseKey.containsKey(reverseKey))
        {
            String key = this.localCacheReverseKey.get(reverseKey);
            this.localCache.get(key).put(EXPIRES_AT, 
                    System.currentTimeMillis() + (Long) this.localCache.get(key).get(TTL));
            return true;
        } else return false;
    }

    public String getKeyByReverseKey(String reverseKey)
    {
        //logger.info(" > localCacheReverseKey: "+localCacheReverseKey);

        return this.localCacheReverseKey.get(reverseKey);
    }

    public JSONArray getKeys()
    {
        return new JSONArray(this.localCache.keySet());
    }

    public Object getValueByKeyAndSubKey(String key, String subKey)
    {
        if (this.containsKey(key))
        {
            if (this.localCache.get(key).containsKey(subKey))
            {
                return this.localCache.get(key).get(subKey);
            } 
            else return null;
        } 
        else return null;
    }

    public void remove(String key)
    {
        Map<String, Object> map = (Map<String, Object>) this.localCache.remove(key);
        ((JSONArray) map.get(REVERSE_KEYs)).forEach(reverseKey -> {
            this.localCacheReverseKey.remove(reverseKey);
        });
    }

    public void removeExpiredTenants()
    {
        this.localCache.keySet().stream()
            .filter(key -> {
                return ((Long) this.localCache.get(key).get(EXPIRES_AT)) > System.currentTimeMillis();
            })
            .collect(Collectors.toList()).forEach(key -> {
                LocalCacheImpl.this.remove(key);
            });
    }
}