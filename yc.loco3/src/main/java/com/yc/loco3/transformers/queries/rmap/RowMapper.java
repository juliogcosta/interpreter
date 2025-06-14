package com.yc.loco3.transformers.queries.rmap;

import java.util.Map;

import org.json.JSONObject;

import com.yc.loco3.Constant;

public class RowMapper
{
    public Integer alias(String entityName, JSONObject model, Map<String, Integer> associationsIndexes)
    {
        final JSONObject entitySpec = model.getJSONObject(entityName);
        if (entitySpec.getJSONObject(Constant._conf).has(Constant.superEntity))
        {
            associationsIndexes.put(entityName,
                    this.alias(entitySpec.getJSONObject(Constant._conf).getString(Constant.superEntity), model, associationsIndexes));
        }
        else
        {
            if (associationsIndexes.containsKey(entityName))
            {
                associationsIndexes.put(entityName, associationsIndexes.get(entityName) + 1);
            }
            else
            {
                associationsIndexes.put(entityName, 0);
            }
        }

        return associationsIndexes.get(entityName);
    }
}
