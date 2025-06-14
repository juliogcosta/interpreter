package com.yc.loco3.transformers.queries.rmap;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yc.loco3.Constant;
import com.yc.loco3.schema.SchemaService;
import com.yc.loco3.transformers.orm.Type.YC;

public class SQLRowMapper extends RowMapper
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    static public String UNDERLINE = "_";

    private SchemaService schemaService;

    public SQLRowMapper(SchemaService schemaService)
    {
        this.schemaService = schemaService;
    }

    public JSONObject getDatas(
            String tenantId, 
            String entityName, 
            JSONObject data, 
            JSONArray dbDatas,
            Boolean count) throws SQLException, Exception
    {
        JSONObject object = new JSONObject();

        //logger.info(" > dbDatas: "+dbDatas.toString(2));

        if (count)
        {
            object.put(entityName, dbDatas.getJSONObject(0).get(Constant.count));
        }
        else
        {
            final JSONArray array = new JSONArray();

            final Map<Long, JSONObject> map = new HashMap<>();

            //logger.info(" > dbDatas: "+dbDatas.toString(2));

            for (int idx = 0; idx < dbDatas.length(); idx++) {
                JSONObject newData = new JSONObject();
                SQLRowMapper.this.mapTree(
                        tenantId, 
                        data.getJSONObject(entityName), 
                        entityName, 
                        null, 
                        newData, 
                        dbDatas.getJSONObject(idx), 
                        map);
                array.put(newData);
            }

            object.put(entityName, array);
        }

        return object;
    }

    private JSONObject mapTree(
            final String tenantId, 
            final JSONObject data, 
            final String entityName, 
            final String entityNameAlias, 
            final JSONObject newData, 
            final JSONObject dbData,
            Map<Long, JSONObject> map) throws Exception
    {
        final JSONObject ooRepresentation = schemaService.getOOStatements(tenantId);

        final JSONObject entity = ooRepresentation.getJSONObject(entityName);

        final JSONObject node = new JSONObject();

        JSONObject associations = entity.getJSONObject(Constant.associations);
        data.keySet().stream().filter(associationName -> associations.has(associationName)).forEach(associationName ->
        {
            final JSONObject association = associations.getJSONObject(associationName);

            newData.put(associationName, new JSONObject());

            try
            {
                node.put("data", this.mapTree(
                        tenantId, 
                        data.getJSONObject(associationName), 
                        association.getString(Constant.type), 
                        associationName, 
                        newData.getJSONObject(associationName), 
                        dbData, 
                        map));
                node.put("ref", associationName);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        final String aliasForEntityName = entityNameAlias == null ? entityName : entityNameAlias;

        final JSONObject attributes = entity.getJSONObject(Constant.attributes);
        final JSONObject components = entity.getJSONObject(Constant.components);

        //logger.info(" > attributes: "+attributes.toString(2));
        attributes.keySet().stream().forEach(key ->
        {
            //logger.info(" > key: "+key);
            String columnName = aliasForEntityName.concat(UNDERLINE).concat(key);

            JSONObject attribute = attributes.getJSONObject(key);

            SQLRowMapper.this.attributeEval(entityName, key, columnName, attribute, dbData, newData);

            //logger.info(" > value: "+newData.get(key));
        });

        components.keySet().stream().forEach(key ->
        {
            newData.put(key, new JSONObject());

            final JSONObject attributesComponent = components.getJSONObject(key).getJSONObject(Constant.attributes);

            data.getJSONObject(key).keySet().forEach(attributeNameComponent -> {
                //logger.info(" > attributeNameComponent: "+attributeNameComponent);
                String columnName = aliasForEntityName.concat(UNDERLINE).concat(attributeNameComponent);

                JSONObject attribute = attributesComponent.getJSONObject(attributeNameComponent);

                SQLRowMapper.this.attributeEval(entityName, attributeNameComponent, columnName, attribute, dbData, newData.getJSONObject(key));

                //logger.info(" > value: "+newData.get(key));
            });
            
            //logger.info(" > value: "+newData.get(key));
        });
        /*
        data.keySet().stream().filter(key -> !associations.has(key)).forEach(key ->
        {
            //logger.info(" > key: "+key);
            
            if (attributes.has(key)) 
            {
                String columnName = aliasForEntityName.concat(UNDERLINE).concat(key);

                JSONObject attribute = attributes.getJSONObject(key);

                SQLRowMapper.this.attributeEval(entityName, key, columnName, attribute, dbData, newData);
            }
            else if (components.has(key)) 
            {
                newData.put(key, new JSONObject());

                final JSONObject attributesComponent = components.getJSONObject(key).getJSONObject(Constant.attributes);

                data.getJSONObject(key).keySet().forEach(attributeNameComponent -> {

                    String columnName = aliasForEntityName.concat(UNDERLINE).concat(attributeNameComponent);

                    JSONObject attribute = attributesComponent.getJSONObject(attributeNameComponent);

                    SQLRowMapper.this.attributeEval(entityName, attributeNameComponent, columnName, attribute, dbData, newData.getJSONObject(key));
                });
            }
            else
            {
                throw new RuntimeException("400:unknown attribute '".concat(key).concat("'."));
            }
            
            //logger.info(" > value: "+newData.get(key));
        });
         */

        if (node.has("data"))
        {
            JSONObject _newData = new JSONObject(newData.toString());
            _newData.remove(node.getString("ref"));

            String _ref = entityName.concat("s");
            if (!node.getJSONObject("data").has(_ref))
            {
                node.getJSONObject("data").put(_ref, new JSONArray());
            }

            JSONArray _refs = node.getJSONObject("data").getJSONArray(_ref);

            int idx = 0;
            for ( ; idx < _refs.length(); idx++)
            {
                if (_refs.getJSONObject(idx).getLong(Constant.id) == _newData.getLong(Constant.id)) 
                {
                    break;
                }
            }

            if (idx == _refs.length())
            {
                _refs.put(_newData);
            }

            return _newData;
        }
        else
        {
            if (newData.length() > 0)
            {
                if (entity.getJSONObject(Constant._conf).has(Constant.partition))
                {
                    return null;
                }
                else if (newData.has(Constant.id))
                {
                    if (!map.containsKey(newData.getLong(Constant.id))) 
                    {
                        map.put(newData.getLong(Constant.id), new JSONObject(newData.toString()));
                    }

                    return map.get(newData.getLong(Constant.id));
                }
                else
                {
                    return null;
                }

                /*
                if (!map.containsKey(newData.getLong(Constant.id))) 
                {
                    map.put(newData.getLong(Constant.id), new JSONObject(newData.toString()));
                }

                return map.get(newData.getLong(Constant.id));*/
            }
            else
            {
                return null;
            }
        }
    }

    private void attributeEval(String entityName, String attributeName, String columnName, JSONObject attribute , JSONObject dbData, JSONObject newData)
    {
        if (dbData.has(columnName))
        {
            switch (attribute.getString(Constant.type))
            {
                case YC.String:
                {
                    newData.put(attributeName, dbData.getString(columnName));

                    break;
                }
                case YC.Date:
                {
                    newData.put(attributeName, dbData.getString(columnName));

                    break;
                }
                case YC.Long:
                {
                    newData.put(attributeName, dbData.getLong(columnName));

                    break;
                }
                case YC.Integer:
                {
                    newData.put(attributeName, dbData.getInt(columnName));

                    break;
                }
                case YC.Money:
                {
                    //logger.info(" > dbData."+columnName+": "+dbData.get(columnName).toString());
                    //logger.info(" > dbData."+columnName+": "+dbData.get(columnName));
                    //logger.info(" > dbData."+columnName+" type "+dbData.get(columnName).getClass().getSimpleName());
                    
                    newData.put(attributeName, dbData.get(columnName).toString());

                    break;
                }
                case YC.Boolean:
                {
                    newData.put(attributeName,
                                dbData.get(columnName) != null ? dbData.getString(columnName).equals("t")
                                        : null);
                    break;
                }
                case YC.Time:
                {
                    newData.put(attributeName, dbData.getString(columnName));

                    break;
                }
                case YC.Timestamp:
                {
                    newData.put(attributeName, dbData.get(columnName));

                    break;
                }
                case YC.Double:
                {
                    newData.put(attributeName, dbData.getDouble(columnName));

                    break;
                }
                case YC.Float:
                {
                    newData.put(attributeName, dbData.getFloat(columnName));

                    break;
                }
                case YC.Text:
                {
                    newData.put(attributeName, dbData.getString(columnName));

                    break;
                }
                default:
                {
                    String message = "400:the attribute '".concat(entityName).concat(".").concat(attributeName).concat("' type unknow.");

                    throw new RuntimeException(message);
                }
            }
        }
    }
}
