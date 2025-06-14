package com.yc.loco3.transformers.mutations;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yc.loco3.Constant;
import com.yc.loco3.schema.SchemaService;
import com.yc.loco3.security.CredentialServiceService;
import com.yc.loco3.transformers.StatementTransformation;
import com.yc.loco3.transformers.orm.Type;

public class CreateStatementTransformation extends StatementTransformation
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    static public String INSERT_INTO_ = "INSERT INTO ";

    static public String _VALUES_ = " VALUES ";

    static public String OPEN_PARENTHESES = "(";

    static public String CLOSE_PARENTHESES = ")";

    static public String DOT = ".";

    static public String WHITESPACE = " ";

    public CreateStatementTransformation(SchemaService schemaService)
    {
        super(schemaService);
    }

    public JSONObject getStatement(String tenantId, JSONObject jsObject, CredentialServiceService authorizationService) throws Exception
    {
        final String entityName = jsObject.keys().next();

        final JSONObject entity = super.schemaService.getOOStatements(tenantId).getJSONObject(entityName);

        if (entity.getJSONObject(Constant._conf).getString(Constant.dbType).equals(Constant.sql)) 
        {
            return getRelationalStatement(tenantId, entityName, jsObject.getJSONObject(entityName), entity, authorizationService);
        }
        else
        {
            return getNonRelationalStatement(tenantId, entityName, jsObject.getJSONObject(entityName), entity, authorizationService);
        }
    }

    private JSONObject getNonRelationalStatement(String tenantId, String entityName, JSONObject data, JSONObject entity, CredentialServiceService authorizationService) throws Exception
    {
        return getRelationalStatement(tenantId, entityName, data, entity, authorizationService);
    }

    private JSONObject getRelationalStatement(String tenantId, String entityName, JSONObject data, JSONObject entity, CredentialServiceService authorizationService) throws Exception
    {
        /*
         * o caso de operar sobre uma entidade particionada não será possível que essa
         * use o serviço de controle de acesso concorrente, dado que por hora não tem 
         * como recuperar o registro referenciado pela via atual (uso do 'id'). 
         * 
         * TODO: alterar o serviço para que ele seja capaz de recuperar com base no 'id'
         * 
         */

        final JSONObject _conf = entity.getJSONObject(Constant._conf);

        /*JSONObject brCallDef = null;
        if (_conf.has(Constant.businessRule) && _conf.getJSONObject(Constant.businessRule).has(Constant.create))
        {
            brCallDef = _conf.getJSONObject(Constant.businessRule).getJSONObject(Constant.create);
            if (brCallDef.has(Constant.before))
            {
                for (int idx = 0; idx < brCallDef.getJSONArray(Constant.before).length(); idx++) 
                {
                    String uri = brCallDef.getJSONArray(Constant.before).getString(idx);
                    try
                    {
                        JSONObject rData = businessRuleService.brCall(uri, "before", "create", data);
                        if (rData.keySet().size() > 0) 
                        {
                            rData.keySet().forEach(field -> {
                                data.put(field, rData.get(field));
                            });
                        }
                        else
                        {
                            throw new RuntimeException("400:the data returned from business rule service is malformed. ");
                        }
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("417?:an error occurred while calling the business rule for data pre-processing [to: ".concat(uri).concat("]."), e);
                    }
                }
            }

            if (!brCallDef.has(Constant.after))
            {
                brCallDef = null;
            }
        }*/

        authorizationService.check(_conf.getJSONObject(Constant.accessControl).getJSONArray(Constant.write));

        data.remove(Constant.id);
        data.put(Constant.loguser, authorizationService.getUsername());
        data.put(Constant.logversion, 0);

        StringBuffer intoClausula = new StringBuffer();
        StringBuffer valuesClausula = new StringBuffer();

        JSONObject sqlIndex = new JSONObject();
        sqlIndex.put(Constant.sqlIndex, 1);

        Collection<JSONObject> sqlStatementParams = new ArrayList<>();

        JSONObject associations = entity.getJSONObject(Constant.associations);
        JSONObject attributes = entity.getJSONObject(Constant.attributes);
        JSONObject components = entity.getJSONObject(Constant.components);

        data.keySet().forEach(keyName -> {
            try
            {
                if (associations.has(keyName)) 
                {
                    JSONObject paramValueObj = new JSONObject();

                    JSONObject associationSpec = associations.getJSONObject(keyName);
                    JSONObject associatedEntity = super.schemaService.getOOStatements(tenantId).getJSONObject(associationSpec.getString(Constant.type));

                    if (data.get(keyName) instanceof JSONObject)
                    {
                        JSONObject associatedObject = data.getJSONObject(keyName);

                        if (associatedObject.has(Constant.id) && !String.valueOf(associatedObject.get(Constant.id)).trim().equals("") && associatedObject.getLong(Constant.id) > 0)
                        {
                            authorizationService.check(associatedEntity.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read));

                            intoClausula.append(keyName.concat(", "));
                            valuesClausula.append("?, ");

                            paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                            paramValueObj.put(Constant.value, associatedObject.get(Constant.id));
                            paramValueObj.put(Constant.type, Type.SQL.Code.BIGINT);

                            sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex)+1);
                        }
                        else if (associatedObject.length() > 1)
                        {
                            intoClausula.append(keyName.concat(", "));
                            valuesClausula.append("?, ");

                            JSONObject innerObj = CreateStatementTransformation.this.getRelationalStatement(
                                    tenantId, 
                                    associationSpec.getString(Constant.type), 
                                    associatedObject, 
                                    associatedEntity, 
                                    authorizationService);
                            innerObj.put(Constant.columnName, keyName);

                            paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                            paramValueObj.put(Constant.value, innerObj);
                            paramValueObj.put(Constant.type, -1);

                            sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex) + 1);
                        }
                        else
                        {
                            throw new Exception("400:the association ".concat(entityName).concat(".").concat(keyName).concat("' is inconsistent. call support."));
                        }
                    }
                    else
                    {
                        authorizationService.check(associatedEntity.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read));

                        intoClausula.append(keyName.concat(", "));
                        valuesClausula.append("?, ");

                        paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                        paramValueObj.put(Constant.value, data.getLong(keyName));
                        paramValueObj.put(Constant.type, Type.SQL.Code.BIGINT);

                        sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex) + 1);
                    }

                    sqlStatementParams.add(paramValueObj);
                }
                else if (attributes.has(keyName)) 
                {
                    intoClausula.append(keyName.concat(", "));
                    valuesClausula.append("?, ");

                    JSONObject attribute = attributes.getJSONObject(keyName);

                    JSONObject paramValueObj = new JSONObject();
                    paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                    paramValueObj.put(Constant.value, data.get(keyName));
                    paramValueObj.put(Constant.type, attribute.getInt(Constant.sqlType));

                    sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex) + 1);

                    sqlStatementParams.add(paramValueObj);
                }
                else if (components.has(keyName)) 
                {
                    JSONObject dataComponent = data.getJSONObject(keyName);
                    JSONObject attributesComponent = components.getJSONObject(keyName).getJSONObject(Constant.attributes);

                    dataComponent.keySet().stream().forEach(attributeName -> {
                        if (attributesComponent.has(attributeName))
                        {
                            JSONObject paramValueObj = new JSONObject();

                            JSONObject componentAttribute = attributesComponent.getJSONObject(attributeName);

                            intoClausula.append(attributeName.concat(", "));
                            valuesClausula.append("?, ");

                            paramValueObj.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex));
                            paramValueObj.put(Constant.value, dataComponent.get(attributeName));
                            paramValueObj.put(Constant.type, componentAttribute.getInt(Constant.sqlType));

                            sqlIndex.put(Constant.sqlIndex, sqlIndex.getInt(Constant.sqlIndex) + 1);

                            sqlStatementParams.add(paramValueObj);
                        }
                        else
                        {
                            throw new RuntimeException("400:unknown component '".concat(keyName).concat("' attribute '").concat(attributeName).concat("'."));
                        }
                    });
                }
                else
                {
                    throw new RuntimeException("400:unknown attribute or component '".concat(keyName).concat("'."));
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        String into = intoClausula.delete(intoClausula.length() - 2, intoClausula.length()).toString();
        String values = valuesClausula.delete(valuesClausula.length() - 2, valuesClausula.length()).toString();

        String statement = INSERT_INTO_.concat("\"").concat(super.schemaService.getSchemaName(tenantId)).concat("\"").concat(DOT).concat(entityName).concat(WHITESPACE)
                .concat(OPEN_PARENTHESES).concat(into).concat(CLOSE_PARENTHESES)
                .concat(_VALUES_).concat(OPEN_PARENTHESES).concat(values).concat(CLOSE_PARENTHESES);

        JSONObject response = new JSONObject();
        response.put(Constant.dbType, _conf.getString(Constant.dbType));
        response.put(Constant.sqlStatement, statement);
        response.put(Constant.sqlStatementParams, new JSONArray(sqlStatementParams));
        response.put(Constant.name, entityName);

        return response;
    }
}
