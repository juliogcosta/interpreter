package com.yc.loco3.transformers.queries.rmap;

import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.yc.loco3.Constant;
import com.yc.loco3.transformers.onrm.Type.YC;

public class NoSQLRowMapper extends RowMapper
{
    public JSONObject getNoSQLData(
            final JSONObject jsObject, 
            final String entityName, 
            final JSONObject model, 
            final JSONArray datas,
            final Boolean allAttribs) throws SQLException, Exception
    {
        JSONObject response = new JSONObject();
        response.put(entityName, new JSONArray());
        for (int idx = 0; idx < datas.length(); idx++)
        {
            response.getJSONArray(entityName).put(this.map(jsObject, entityName, datas.getJSONObject(idx), model, allAttribs));
        }
        return response;
    }

    private JSONObject map(
            final JSONObject jsObject,
            final String entityName,
            final JSONObject dbData, 
            final JSONObject model,
            final Boolean allAttribs)
    {
        final JSONObject data = new JSONObject();

        final JSONObject entitySpec = model.getJSONObject(entityName);

        entitySpec.keySet().forEach(attributeName -> 
        {
            if (attributeName.equals(Constant._conf))
            {

            }
            else if (allAttribs)
            {
                switch (entitySpec.getJSONObject(attributeName).getString(Constant.type))
                {
                    case YC.String:
                    {
                        data.put(attributeName, dbData.getString(attributeName));

                        break;
                    }
                    case YC.Boolean:
                    {
                        data.put(attributeName, dbData.getString(attributeName).equals("t"));

                        break;
                    }
                    case YC.Time:
                    {
                        data.put(attributeName, dbData.getLong(attributeName));

                        break;
                    }
                    case YC.Long:
                    {
                        data.put(attributeName, dbData.getLong(attributeName));

                        break;
                    }
                    case YC.Integer:
                    {
                        data.put(attributeName, dbData.getInt(attributeName));

                        break;
                    }
                    case YC.Double:
                    {
                        data.put(attributeName, dbData.getDouble(attributeName));

                        break;
                    }
                    case YC.Float:
                    {
                        data.put(attributeName, dbData.getFloat(attributeName));

                        break;
                    }
                    case YC.Date:
                    {
                        data.put(attributeName, dbData.getString(attributeName));

                        break;
                    }
                    case YC.Timestamp:
                    {
                        data.put(attributeName, dbData.getString(attributeName));

                        break;
                    }
                    default:
                    {
                        /*Logger.getLogger(getClass().getSimpleName())
                            .info("error: object attribute '".concat(entityName).concat(".").concat(attributeName)
                                    .concat("' type unknow"));*/
                    }
                }
            }
            else if (jsObject.has(attributeName))
            {
                switch (entitySpec.getJSONObject(attributeName).getString(Constant.type))
                {
                    case YC.String:
                    {
                        data.put(attributeName, dbData.getString(attributeName));

                        break;
                    }
                    case YC.Boolean:
                    {
                        data.put(attributeName, dbData.getString(attributeName).equals("t"));

                        break;
                    }
                    case YC.Time:
                    {
                        data.put(attributeName, dbData.getLong(attributeName));

                        break;
                    }
                    case YC.Long:
                    {
                        data.put(attributeName, dbData.getLong(attributeName));

                        break;
                    }
                    case YC.Integer:
                    {
                        data.put(attributeName, dbData.getInt(attributeName));

                        break;
                    }
                    case YC.Double:
                    {
                        data.put(attributeName, dbData.getDouble(attributeName));

                        break;
                    }
                    case YC.Float:
                    {
                        data.put(attributeName, dbData.getFloat(attributeName));

                        break;
                    }
                    case YC.Date:
                    {
                        data.put(attributeName, dbData.getString(attributeName));

                        break;
                    }
                    case YC.Timestamp:
                    {
                        data.put(attributeName, dbData.getString(attributeName));

                        break;
                    }
                    default:
                    {
                        /*Logger.getLogger(getClass().getSimpleName())
                            .info("error: object attribute '".concat(entityName).concat(".").concat(attributeName)
                                        .concat("' type unknow"));*/
                    }
                }
            }
        });
        
        return data;
    }
}
