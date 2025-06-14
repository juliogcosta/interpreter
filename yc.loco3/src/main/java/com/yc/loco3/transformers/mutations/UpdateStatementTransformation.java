package com.yc.loco3.transformers.mutations;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yc.loco3.Constant;
import com.yc.loco3.dao.DAO;
import com.yc.loco3.schema.SchemaService;
import com.yc.loco3.security.CredentialServiceService;
import com.yc.loco3.transformers.StatementTransformation;
import com.yc.loco3.transformers.orm.Type;

public class UpdateStatementTransformation extends StatementTransformation
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    static private String UPDATE_ = "UPDATE ";

    static private String _SET_ = " SET ";

    static private String DOT = ".";

    static private String _WHERE_ = " WHERE ";

    static private String _EQUALS_ = " = ";

    static private String _AND_ = " AND ";

    private DAO dao;

    public UpdateStatementTransformation(SchemaService schemaService, DAO dao)
    {
        super(schemaService);

        this.dao = dao;
    }

    public JSONObject getStatement(String traceId, String spanId, String parentSpanId, String tenantId, JSONObject jsObject, CredentialServiceService authorizationService) throws Exception
    {
        final String entityName = jsObject.keys().next();

        final JSONObject data = jsObject.getJSONObject(entityName);

        final JSONObject entitySpec = super.schemaService.getOOStatements(tenantId).getJSONObject(entityName);

        if (entitySpec.getJSONObject(Constant._conf).getString(Constant.dbType).equals(Constant.sql)) 
        {
            return getRelationalStatement(traceId, spanId, parentSpanId, tenantId, entityName, data, entitySpec, authorizationService);
        }
        else
        {
            return getNonRelationalStatement(traceId, spanId, parentSpanId, tenantId, entityName, data, entitySpec, authorizationService);
        }
    }

    private JSONObject getNonRelationalStatement(String traceId, String spanId, String parentSpanId, String tenantId, String entityName, JSONObject data, JSONObject entitySpec, CredentialServiceService authorizationService) throws Exception
    {
        return getRelationalStatement(traceId, spanId, parentSpanId, tenantId, entityName, data, entitySpec, authorizationService);
    }

    private JSONObject getRelationalStatement(String traceId, String spanId, String parentSpanId, String tenantId, String entityName, JSONObject data, JSONObject entitySpec, CredentialServiceService authorizationService) throws Exception
    {
        /*
         * o caso de operar sobre uma entidade particionada não será possível que essa
         * use o serviço de controle de acesso concorrente, dado que por hora não tem 
         * como recuperar o registro referenciado pela via atual (uso do 'id'). 
         * 
         * TODO: alterar o serviço para que ele seja capaz de recuperar com base no 'id'
         * 
         */

        final JSONObject _conf = entitySpec.getJSONObject(Constant._conf);

        String pkName = null;

        authorizationService.check(_conf.getJSONObject(Constant.accessControl).getJSONArray(Constant.write));

        final JSONObject primaryKey = new JSONObject();
        final Set<String> attributeNamePKs = new HashSet<>();

        final Boolean isSQL = _conf.getString(Constant.dbType).equals(Constant.sql);
        if (isSQL) 
        {
            if (_conf.has(Constant.partition))
            {
                pkName = _conf.getJSONObject(Constant.partition).getString(Constant.by);
                primaryKey.put(pkName, data.get(pkName));

                attributeNamePKs.add(pkName);
            }
            else
            {
                pkName = Constant.id;

                primaryKey.put(Constant.id, data.remove(Constant.id));

                attributeNamePKs.add(Constant.id);
            }
        }
        else 
        {
            JSONObject _data = new JSONObject();

            _conf.getJSONObject(Constant.uniqueKey).getJSONArray(Constant.partitionKeys).forEach(partitionKey -> {
                JSONObject descriptor = new JSONObject();
                descriptor.put("spec", entitySpec.getJSONObject(partitionKey.toString()));
                descriptor.put("value", data.get(partitionKey.toString()));
                primaryKey.put(partitionKey.toString(), descriptor);

                attributeNamePKs.add(partitionKey.toString());
                _data.put(partitionKey.toString(), descriptor.get("value"));
            });

            _conf.getJSONObject(Constant.uniqueKey).getJSONArray(Constant.clusteringColumns).forEach(clusteringColumn -> {
                JSONObject descriptor = new JSONObject();
                descriptor.put("spec", entitySpec.getJSONObject(clusteringColumn.toString()));
                descriptor.put("value", data.get(clusteringColumn.toString()));
                primaryKey.put(clusteringColumn.toString(), descriptor);

                attributeNamePKs.add(clusteringColumn.toString());
                _data.put(clusteringColumn.toString(), descriptor.get("value"));
            });
        }

        final JSONObject dataDb;
        if (_conf.getBoolean(Constant.concurrencyControl))
        {
            final JSONObject newDat = new JSONObject(data.toString());
            //logger.info(" > "+getClass().getCanonicalName()+": newData: "+newDat.toString(2));

            nullable(newDat, entitySpec, super.schemaService.getOOStatements(tenantId));

            JSONObject xJSObject = new JSONObject();
            xJSObject.put(entityName, newDat);

            dataDb = this.dao.readById(traceId, spanId, parentSpanId, tenantId, xJSObject);

            data.put(Constant.logversion, dataDb.getInt(Constant.logversion) + 1);
        }
        else
        {
            dataDb = null;

            data.put(Constant.logversion, 0);
        }

        attributeNamePKs.add(Constant._conf);

        JSONObject sqlIndex = new JSONObject();
        sqlIndex.put(Constant.sqlIndex, 1);

        StringBuffer setClausula = new StringBuffer();

        JSONObject associations = entitySpec.getJSONObject(Constant.associations);
        JSONObject attributes = entitySpec.getJSONObject(Constant.attributes);
        JSONObject components = entitySpec.getJSONObject(Constant.components);

        JSONArray paramValueObjs = new JSONArray();

        data.keySet().forEach(keyName -> {
            try
            {
                if (associations.has(keyName)) 
                {
                    JSONObject paramValueObj = new JSONObject();

                    JSONObject associationSpec = associations.getJSONObject(keyName);

                    JSONObject associatedEntitySpec = super.schemaService.getOOStatements(tenantId).getJSONObject(associationSpec.getString(Constant.type));

                    //logger.info("association '".concat(entityName).concat(".").concat(keyName));

                    if (associatedEntitySpec.getJSONObject(Constant._conf).getBoolean(Constant.concurrencyControl))
                    {
                        JSONObject associatedObject = data.getJSONObject(keyName);
                        if (associatedObject.has(Constant.id))
                        {
                            if (associatedObject.length() == 1)
                            {
                                authorizationService.check(associatedEntitySpec.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read));

                                JSONObject auxiliar = new JSONObject();
                                auxiliar.put(associationSpec.getString(Constant.type), associatedObject);
                                if (associatedObject.getInt(Constant.logversion) != dataDb.getInt(Constant.logversion)) 
                                {
                                    throw new Exception("409:register version does not match for association '".concat(keyName).concat("'."));
                                }
                                paramValueObj.put(Constant.name, keyName);
                                paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                                paramValueObj.put(Constant.value, associatedObject.get(Constant.id));
                                paramValueObj.put(Constant.type, Type.SQL.Code.BIGINT);

                                setClausula.append(keyName).append(" = ?, ");

                                sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex)+1);
                            }
                            else
                            {
                                if (data.has(Constant.logrole)) {
                                    associatedObject.put(Constant.logrole, data.getString(Constant.logrole));
                                }

                                JSONObject wrapper = new JSONObject();
                                wrapper.put(associationSpec.getString(Constant.type), associatedObject);

                                paramValueObj.put(Constant.name, keyName);
                                paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                                paramValueObj.put(Constant.value, UpdateStatementTransformation.this.getRelationalStatement(traceId, 
                                        spanId, 
                                        parentSpanId, 
                                        tenantId, 
                                        associationSpec.getString(Constant.type), 
                                        wrapper, 
                                        associatedEntitySpec, 
                                        authorizationService));
                                paramValueObj.put(Constant.type, -1);
                            }
                        }
                        else
                        {
                            throw new Exception("400:there is inconsistency in the '".concat(entityName).concat(".").concat(keyName).concat("' association.")
                                .concat("'. it needs at least 'id' attribute."));
                        }
                    }
                    else if (data.get(keyName) instanceof JSONObject)
                    {
                        JSONObject associatedObject = data.getJSONObject(keyName);

                        //logger.info("value for association '".concat(entityName).concat(".").concat(keyName)+": "+associatedObject.toString(2));

                        if (associatedObject.has(Constant.id)) 
                        {
                            if (associatedObject.length() == 1) 
                            {
                                authorizationService.check(associatedEntitySpec.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read));

                                paramValueObj.put(Constant.name, keyName);
                                paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));

                                /*
                                 * TODO: avaliar pkName no lugar de Constant.id. avaliar o caso em que o id não é a chave primária.
                                 *       avaliar mudar o serviço de interpretação para não criar e usar id compulsoriamente.
                                 * pkName, data.getJSONObject(keyName).getLong(Constant.id)
                                 * 
                                 */
                                paramValueObj.put(Constant.value, data.getJSONObject(keyName).getLong(Constant.id));
                                paramValueObj.put(Constant.type, Type.SQL.Code.BIGINT);

                                setClausula.append(keyName).append(" = ?, ");

                                sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex)+1);
                            }
                            else 
                            {
                                authorizationService.check(associatedEntitySpec.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.write));

                                //logger.info("[recursive calling] value for association '".concat(entityName).concat(".").concat(keyName)+": "+associatedObject.toString(2));

                                if (data.has(Constant.logrole)) {
                                    associatedObject.put(Constant.logrole, data.getString(Constant.logrole));
                                }

                                /*
                                logger.info(" > entityName: "+entityName);
                                logger.info(" > data: "+data.toString(2));
                                logger.info(" > entity: "+entitySpec.toString(2));

                                logger.info(" > new entityName [associationSpec.getString(Constant.type)]: "+associationSpec.getString(Constant.type));
                                logger.info(" > new data [associatedObject]: "+associatedObject.toString(2));
                                logger.info(" > new entity [associatedEntitySpec]: "+associatedEntitySpec.toString(2));
                                 */

                                paramValueObj.put(Constant.name, keyName);
                                paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                                paramValueObj.put(Constant.value, UpdateStatementTransformation.this.getRelationalStatement(traceId, 
                                        spanId, 
                                        parentSpanId, 
                                        tenantId, 
                                        associationSpec.getString(Constant.type), 
                                        associatedObject, 
                                        associatedEntitySpec, 
                                        authorizationService));
                                paramValueObj.put(Constant.type, -1);

                                //logger.info("paramValueObj for association '".concat(entityName).concat(".").concat(keyName)+": "+paramValueObj.toString(2));
                            }
                        } 
                        else 
                        {
                            throw new Exception("400:there is inconsistency in the '".concat(entityName).concat(".").concat(keyName).concat("' association.")
                                .concat("'. it needs at least 'id' attribute."));
                        }
                    }
                    else 
                    {
                        authorizationService.check(associatedEntitySpec.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read));

                        paramValueObj.put(Constant.name, keyName);
                        paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                        paramValueObj.put(Constant.value, data.getLong(keyName));
                        paramValueObj.put(Constant.type, Type.SQL.Code.BIGINT);

                        setClausula.append(keyName).append(" = ?, ");

                        sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex)+1);
                    }

                    paramValueObjs.put(paramValueObj);
                }
                else if (attributes.has(keyName))
                {
                    JSONObject paramValueObj = new JSONObject();

                    JSONObject attribute = attributes.getJSONObject(keyName);

                    paramValueObj.put(Constant.name, keyName);
                    paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                    paramValueObj.put(Constant.value, data.get(keyName));
                    paramValueObj.put(Constant.type, attribute.getInt(Constant.sqlType));

                    setClausula.append(keyName).append(" = ?, ");

                    sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex) + 1);

                    paramValueObjs.put(paramValueObj);
                }
                else if (components.has(keyName))
                {
                    final JSONObject dataComponent = data.getJSONObject(keyName);
                    final JSONObject attributesComponent = components.getJSONObject(keyName).getJSONObject(Constant.attributes);

                    dataComponent.keySet().forEach(componentAttributeName -> {
                        if (attributesComponent.has(componentAttributeName))
                        {
                            JSONObject paramValueObj = new JSONObject();

                            JSONObject attribute = attributesComponent.getJSONObject(componentAttributeName);

                            paramValueObj.put(Constant.name, componentAttributeName);
                            paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                            paramValueObj.put(Constant.value, data.getJSONObject(keyName).get(componentAttributeName));
                            paramValueObj.put(Constant.type, attribute.getInt(Constant.sqlType));

                            setClausula.append(componentAttributeName).append(" = ?, ");

                            sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex) + 1);

                            paramValueObjs.put(paramValueObj);
                        }
                        else
                        {
                            throw new RuntimeException("400:unknown component '".concat(keyName).concat("' attribute '").concat(componentAttributeName).concat("'."));
                        }
                    });
                }
                else 
                {
                    throw new RuntimeException("400:unknown attribute or component '".concat(entityName).concat(".").concat(keyName).concat("'."));
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        JSONObject response = new JSONObject();

        if (isSQL) 
        {
            String statement = UPDATE_.concat("\"").concat(super.schemaService.getSchemaName(tenantId)).concat("\"").concat(DOT).concat(entityName)
                    .concat(_SET_).concat(setClausula.delete(setClausula.length() - 2, setClausula.length()).toString())
                    .concat(_WHERE_).concat(pkName).concat(_EQUALS_).concat(primaryKey.get(pkName).toString());

            if (_conf.getBoolean(Constant.concurrencyControl))
            {
                response.put(Constant.sqlStatement, statement
                        .concat(_AND_).concat(Constant.logversion).concat(_EQUALS_).concat(String.valueOf(dataDb.getInt(Constant.logversion))));
            }
            else
            {
                response.put(Constant.sqlStatement, statement);
            }

            response.put(Constant.sqlStatementParams, paramValueObjs);
            response.put(Constant.instanceId, primaryKey.getLong(pkName));
        }
        else
        {
            StringBuffer filterBuffer = new StringBuffer();
            primaryKey.keySet().forEach(key -> {
                Object value = primaryKey.getJSONObject(key).get("value");
                String type = primaryKey.getJSONObject(key).getJSONObject("spec").getString(Constant.type);
                String fValue = Type.getInstance().getValueFormattedByType(value, type);

                filterBuffer.append(key.toString()).append(_EQUALS_).append(fValue).append(_AND_);
            });

            StringBuffer columnBuffer = new StringBuffer();
            paramValueObjs.forEach(item -> {
                JSONObject paramValueObj = (JSONObject) item;
                Object param = paramValueObj.remove(Constant.name);
                Object value= paramValueObj.get(Constant.value);
                columnBuffer.append(param).append(_EQUALS_).append(value).append(", ");
            });
            columnBuffer.delete(columnBuffer.length() - 2, columnBuffer.length());

            String statement = UPDATE_.concat(entityName).concat(_SET_).concat(columnBuffer.toString());
            if (entitySpec.getJSONObject(Constant._conf).getBoolean(Constant.concurrencyControl))
            {
                response.put(Constant.sqlStatement, statement
                        .concat(filterBuffer.toString())
                        .concat(Constant.logversion).concat(_EQUALS_).concat(data.get(Constant.logversion).toString()));
            }
            else
            {
                filterBuffer.delete(filterBuffer.length() - 5, filterBuffer.length());
                response.put(Constant.sqlStatement, statement
                        .concat(filterBuffer.toString()));
            }
        }

        response.put(Constant.dbType, _conf.getString(Constant.dbType));
        response.put(Constant.name, entityName);

        return response;
    }

    private void nullable(JSONObject dataObject, JSONObject entity, JSONObject schema)
    {
        if (!dataObject.has(Constant.logversion)) 
        {
            dataObject.put(Constant.logversion, "");
        }
        //logger.info(" > nullable(): dataObject: "+dataObject.toString(2));
        //logger.info(" > nullable(): entity: "+entity.toString(2));
        final JSONObject associations = entity.getJSONObject(Constant.associations);
        final JSONObject attributes = entity.getJSONObject(Constant.attributes);
        final JSONObject components = entity.getJSONObject(Constant.components);

        dataObject.keySet().stream()
            .filter(keyName -> !keyName.equals(Constant.id))
            .forEach(keyName -> {
                //logger.info(" > nullable(): keyName: "+keyName);
                if (associations.has(keyName))
                {
                    if (!(dataObject.get(keyName) instanceof JSONObject)) 
                    {
                        JSONObject data = new JSONObject();
                        data.put(Constant.id, dataObject.get(keyName));
                        dataObject.put(keyName, data);
                    }

                    nullable(dataObject.getJSONObject(keyName), schema.getJSONObject(associations.getJSONObject(keyName).getString(Constant.type)), schema);
                }
                else if (attributes.has(keyName))
                {
                    dataObject.put(keyName, "");
                }
                else if (components.has(keyName))
                {
                    components.getJSONObject(keyName).getJSONObject(Constant.attributes).keySet().forEach(componentAttributeName -> {
                        dataObject.getJSONObject(keyName).put(componentAttributeName, "");
                    });
                }
                else
                {
                    throw new RuntimeException("400:object representation is inconsistency.");
                }
            });
    }
}
