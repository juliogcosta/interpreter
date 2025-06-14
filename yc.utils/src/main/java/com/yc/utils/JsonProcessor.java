package com.yc.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;

public class JsonProcessor 
{
    public JSONObject removeNullKeysRecursively(String jsonString)
    {
        JSONObject jsonObject = new JSONObject(jsonString);

        return removeNullKeysRecursively(jsonObject);
    }

    public JSONObject removeNullKeysRecursively(JSONObject jsonObject)
    {
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext())
        {
            String key = keys.next();
            Object value = jsonObject.get(key);

            if (value instanceof JSONObject)
            {
                JSONObject childObject = (JSONObject) value;
                removeNullKeysRecursively(childObject);

                if (childObject.isEmpty())
                {
                    keys.remove();
                }
            }
            else if (value instanceof JSONArray)
            {
                JSONArray jsonArray = (JSONArray) value;
                removeNullValuesFromArray(jsonArray);

                if (jsonArray.isEmpty())
                {
                    keys.remove();
                }
            }
            else if (jsonObject.isNull(key))
            {
                keys.remove();
            }
        }

        return jsonObject;
    }

    public void removeNullValuesFromArray(JSONArray jsonArray)
    {
        for (int i = 0; i < jsonArray.length(); i++)
        {
            Object element = jsonArray.get(i);

            if (element instanceof JSONObject)
            {
                JSONObject childObject = (JSONObject) element;
                removeNullKeysRecursively(childObject);

                if (childObject.isEmpty())
                {
                    jsonArray.remove(i);
                    i--;
                }
            }
            else if (element instanceof JSONArray)
            {
                JSONArray childArray = (JSONArray) element;
                removeNullValuesFromArray(childArray);

                if (childArray.isEmpty())
                {
                    jsonArray.remove(i);
                    i--;
                }
            }
            else if (element == JSONObject.NULL)
            {
                jsonArray.remove(i);
                i--;
            }
        }
    }
}
