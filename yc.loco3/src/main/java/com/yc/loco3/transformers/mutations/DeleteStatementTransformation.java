package com.yc.loco3.transformers.mutations;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yc.loco3.Constant;
import com.yc.loco3.schema.SchemaService;
import com.yc.loco3.security.CredentialServiceService;
import com.yc.loco3.transformers.StatementTransformation;
import com.yc.loco3.transformers.orm.Type;

public class DeleteStatementTransformation extends StatementTransformation
{
    Logger logger = LoggerFactory.getLogger(getClass());

    static private String DELETE_FROM_ = "DELETE FROM ";

    static public String WHITESPACE = " ";

    static private String NULLCHAR = "";

    static private String _WHERE_ = " WHERE ";

    static private String _AND_ = " AND ";

    static private String _EQUALS_ = " = ";

    static private String _IS_ = " IS ";

    static private String EQUALS = " = ";

    static private String DOT = ".";

    static public String COMMA = ",";

    static public String UNDERLINE = "_";

    static private String OPEN_PARENTHESES = ".";

    static private String CLOSE_PARENTHESES = ".";

    public DeleteStatementTransformation(SchemaService schemaService)
    {
        super(schemaService);
    }

    public JSONObject getStatement(String tenantId, JSONObject jsObject, CredentialServiceService authorizationService) throws Exception
    {
        final String connective;
        if (jsObject.has(Constant._connective))
        {
            connective = jsObject.getString(Constant._connective);
            jsObject.remove(Constant._connective);
        }
        else
        {
            connective = "AND";
        }

        String entityName = jsObject.keys().next();

        JSONObject data = jsObject.getJSONObject(entityName);

        //if (data.keySet().size() == 0)
        //{
            //throw new Exception("400:no highlighted attribute(s) for data retrieval.");
        //}

        /*
         * JSONObject schema = super.schemaService.getOOStatements(tenantId);
         */

        JSONObject entity = super.schemaService.getOOStatements(tenantId).getJSONObject(entityName);

        JSONObject _conf = entity.getJSONObject(Constant._conf);

        authorizationService.check(_conf.getJSONObject(Constant.accessControl).getJSONArray(Constant.write));

        /*
        if (_conf.has(Constant.partition))
        {
            if (data.has(_conf.getJSONObject(Constant.partition).getString(Constant.by)) && !data.has(Constant.logrole))
            {
                throw new Exception("400:the attribute '".concat(Constant.logrole).concat("' is not defined."));
            }
        }
        else
        {
            if (data.has(Constant.id) && !data.has(Constant.logrole))
            {
                throw new Exception("400:the attribute '".concat(Constant.logrole).concat("' is not defined."));
            }
        }
         */

        JSONObject response = new JSONObject();;

        if (_conf.getString(Constant.dbType).equals(Constant.sql)) 
        {
            response.put(Constant.sqlStatement, this.getRelationalStatement(tenantId, entityName, data, connective, entity, authorizationService));
        }
        else
        {
            response.put(Constant.sqlStatement, this.getNonRelationalStatement(tenantId, entityName, data, entity, authorizationService));
        }

        /*
        JSONArray paramValueObjs = new JSONArray(entity.keySet().parallelStream().filter(attributeName -> {
            return (data.has(attributeName) && schema.has(entity.getJSONObject(attributeName).getString(Constant.type)));
        }).collect(Collectors.toMap(attributeName -> attributeName, attributeName -> {
            try
            {
                JSONObject associatedData = data.getJSONObject(attributeName);
                if (associatedData.has(Constant.id))
                {
                    JSONObject attributeSpec = entity.getJSONObject(attributeName);

                    JSONObject associatedEntitySpec = super.schemaService.getOOStatements(tenantId).getJSONObject(attributeSpec.getString(Constant.type));

                    authorizationService.check(associatedEntitySpec.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.write));

                    JSONObject wrapper = new JSONObject();
                    wrapper.put(attributeSpec.getString(Constant.type), associatedData);
                    wrapper.put(Constant._connective, connective);

                    JSONObject paramValueObj = new JSONObject();
                    paramValueObj.put(Constant.sqlIndex, attributeSpec.getInt(Constant.sqlIndex));
                    paramValueObj.put(Constant.value, DeleteStatementTransformation.this.getStatement(tenantId, wrapper, authorizationService, businessRuleService));
                    paramValueObj.put(Constant.type, -1);
                    return paramValueObj;
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }

            return new JSONObject();
        })).values());
        response.put(Constant.sqlStatementParams, paramValueObjs);
         */

        response.put(Constant.dbType, entity.getJSONObject(Constant._conf).getString(Constant.dbType));
        response.put(Constant.name, entityName);

        return response;
    }

    private String getNonRelationalStatement(String tenantId, String entityName, JSONObject data, JSONObject entitySpec, CredentialServiceService authorizationService) throws Exception
    {
        JSONObject primaryKey = new JSONObject();

        entitySpec.getJSONObject(Constant._conf).getJSONObject(Constant.uniqueKey).getJSONArray(Constant.partitionKeys).forEach(partitionKey -> {
            JSONObject descriptor = new JSONObject();
            descriptor.put("spec", entitySpec.getJSONObject(partitionKey.toString()));
            descriptor.put("value", data.remove(partitionKey.toString()));
            primaryKey.put(partitionKey.toString(), descriptor);
        });

        entitySpec.getJSONObject(Constant._conf).getJSONObject(Constant.uniqueKey).getJSONArray(Constant.clusteringColumns).forEach(clusteringColumn -> {
            JSONObject descriptor = new JSONObject();
            descriptor.put("spec", entitySpec.getJSONObject(clusteringColumn.toString()));
            descriptor.put("value", data.remove(clusteringColumn.toString()));
            primaryKey.put(clusteringColumn.toString(), data.remove(clusteringColumn.toString()));
        });

        StringBuffer buffer = new StringBuffer();
        primaryKey.keySet().forEach(key -> {
            Object value = primaryKey.get("value");
            String type = primaryKey.getJSONObject("spec").getString(Constant.type);
            String fValue = Type.getInstance().getValueFormattedByType(value, type);
            buffer.append(key.toString()).append(EQUALS).append(fValue).append(_AND_);
        });

        String statement = DELETE_FROM_.concat(entityName);
        if (entitySpec.getJSONObject(Constant._conf).getBoolean(Constant.concurrencyControl))
        {
            return statement.concat(_WHERE_).concat(buffer.toString()).concat(Constant.logversion)
                    .concat(EQUALS).concat(data.remove(Constant.logversion).toString());
        }
        else if (buffer.length() > 2)
        {
            buffer.delete(buffer.length() - 5, buffer.length());
            return statement.concat(_WHERE_).concat(buffer.toString());
        }
        else
        {
            return statement;
        }
    }

    private String getRelationalStatement(String tenantId, String entityName, JSONObject data, String connective, JSONObject entitySpec,  CredentialServiceService userAuthorizationService) throws Exception
    {
        String statement = DELETE_FROM_.concat("\"").concat(super.schemaService.getSchemaName(tenantId)).concat("\"").concat(DOT).concat(entityName);

        final JSONObject _conf = entitySpec.getJSONObject(Constant._conf);

        final Boolean concurrencyControl = _conf.getBoolean(Constant.concurrencyControl);
        if (concurrencyControl)
        {
            if (!data.has(Constant.logversion))
            {
                throw new Exception("400:the attribute '".concat(entityName).concat(DOT).concat(Constant.logversion).concat("' cannot be null or it might not be found."));
            }
        }

        final String pkName;
        if (_conf.has(Constant.partition))
        {
            pkName = _conf.getJSONObject(Constant.partition).getString(Constant.by);
        }
        else if (data.has(Constant.id))
        {
            pkName = Constant.id;
        }
        else
        {
            pkName = null;
        }

        if (pkName == null)
        {
            if (data.length() > 0) 
            {
                final List<String> toFilter = new ArrayList<String>();

                this.configSQLQuery(
                        super.schemaService.getSchemaName(tenantId),
                        super.schemaService.getOOStatements(tenantId),
                        entityName, 
                        null, 
                        data, 
                        connective, 
                        toFilter, 
                        userAuthorizationService);

                StringBuffer whereClausula = new StringBuffer();
                if (toFilter.size() > 0)
                {
                    toFilter.forEach(item -> whereClausula.append(item));

                    if (connective.equals("OR"))
                    {
                        whereClausula.delete(whereClausula.length() - 4, whereClausula.length());
                    }
                    else
                    {
                        whereClausula.delete(whereClausula.length() - 5, whereClausula.length());
                    }
                }

                String query = statement.concat(
                    (whereClausula.length() > 0 ? 
                          _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) 
                        : NULLCHAR));

                return query;
            }
            else 
            {
                return statement;
            }
        }
        else
        {
            if (concurrencyControl)
            {
                return statement.concat(_WHERE_)
                        .concat(Constant.id).concat(EQUALS).concat(data.remove(Constant.id).toString())
                        .concat(_AND_)
                        .concat(Constant.logversion).concat(EQUALS).concat(data.remove(Constant.logversion).toString());
            }
            else
            {
                return statement.concat(_WHERE_).concat(pkName).concat(EQUALS).concat(data.remove(pkName).toString());
            }
        }
    }

    private void configSQLQuery(
            final String schemaName,
            final JSONObject schema,
            final String entityName, 
            final String entityNameAlias, 
            final JSONObject data, 
            final String connective, 
            final List<String> whereClausula, 
            final CredentialServiceService userAuthorizationService) throws Exception
    {
        final JSONObject entity = schema.getJSONObject(entityName);

        userAuthorizationService.check(entity.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read));

        try
        {
            final String aliasForEntityName = entityNameAlias == null ? entityName : entityNameAlias;

            final JSONObject associations = entity.getJSONObject(Constant.associations);
            final JSONObject attributes = entity.getJSONObject(Constant.attributes);
            final JSONObject components = entity.getJSONObject(Constant.components);

            if (!data.has(Constant.id) && !entity.getJSONObject(Constant._conf).has(Constant.partition))
            {
                    data.put(Constant.id, "");
            }

            data.keySet().stream().forEach(key -> {
                try
                {
                    if (associations.has(key))
                    {
                        final String associationEntityName = associations.getJSONObject(key).getString(Constant.type);

                        JSONObject assoc;

                        if (data.get(key) instanceof JSONObject)
                        {
                            assoc = data.getJSONObject(key);
                        }
                        else
                        {
                            assoc = new JSONObject();
                            assoc.put(Constant.id, data.getLong(key));
                        }

                        this.configSQLQuery(schemaName,
                                schema, 
                                associationEntityName, 
                                key, 
                                data.getJSONObject(key), 
                                connective, 
                                whereClausula, 
                                userAuthorizationService);
                    }
                    else if (attributes.has(key)) 
                    {
                        DeleteStatementTransformation.this.extractPredicates(
                                entityName, 
                                aliasForEntityName, 
                                key, 
                                attributes, 
                                data, 
                                connective, 
                                whereClausula);
                    }
                    else if (components.has(key)) 
                    {
                        JSONObject dataComponent = data.getJSONObject(key);
                        JSONObject attributesComponent = components.getJSONObject(key).getJSONObject(Constant.attributes);

                        dataComponent.keySet().forEach(attributeNameComponent -> {
                            try
                            {
                                if (attributesComponent.has(attributeNameComponent)) 
                                {
                                    DeleteStatementTransformation.this.extractPredicates(
                                            entityName, 
                                            aliasForEntityName, 
                                            attributeNameComponent, 
                                            attributesComponent, 
                                            dataComponent, 
                                            connective, 
                                            whereClausula);
                                }
                                else
                                {
                                    throw new RuntimeException("400:unknown component '".concat(key).concat("' attribute '").concat(attributeNameComponent).concat("'."));
                                }
                            }
                            catch (Exception e)
                            {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    else
                    {
                        throw new RuntimeException("400:unknown attribute '".concat(key).concat("'."));
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            });
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    private void extractPredicates(String entityName, 
            String entityNameAlias, 
            String attributeName, 
            JSONObject attributes, 
            JSONObject data, 
            String connective, 
            List<String> whereClausula) throws Exception
    {
        String attributeType = attributes.getJSONObject(attributeName).getString(Constant.type);

        StringBuffer operator = new StringBuffer();
        String innerConnective = _AND_;
        String value = null;
        Integer flag = 0;
        String supValue = null;

        if (data.get(attributeName) instanceof JSONObject)
        {
            JSONObject dataAttribute = data.getJSONObject(attributeName);

            Set<String> predicates = dataAttribute.keySet();
            if (predicates.size() == 1)
            {
                String op = dataAttribute.keys().next();
                switch (op)
                {
                    case Constant.Operator.eq.name:
                    {
                        operator.append(Constant.Operator.eq.symbol);
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.ilike.name:
                    {
                        if (attributeType.equals(Type.YC.String) || attributeType.equals(Type.YC.Text))
                        {
                            if (dataAttribute.getString(op).contains("%"))
                            {
                                operator.append(Constant.Operator.ilike.symbol);
                            }
                            else
                            {
                                throw new Exception("400:missing the '%' symbol to ilike operator ('".concat(entityName).concat(DOT).concat(attributeName)
                                        .concat("' attribute)."));
                            }
                        }
                        else
                        {
                            throw new Exception("400:operator usage is not compatible with the attribute '".concat(entityName).concat(DOT).concat(attributeName)
                                    .concat("' type '").concat(attributeType).concat("'"));
                        }
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.like.name:
                    {
                        if (attributeType.equals(Type.YC.String) || attributeType.equals(Type.YC.Text))
                        {
                            if (dataAttribute.getString(op).contains("%"))
                            {
                                operator.append(Constant.Operator.like.symbol);
                            }
                            else
                            {
                                throw new Exception("400:missing the '%' symbol to like operator ('".concat(entityName).concat(DOT).concat(attributeName)
                                        .concat("' attribute)."));
                            }
                        }
                        else
                        {
                            throw new Exception("400:operator usage is not compatible with the attribute '".concat(entityName).concat(DOT).concat(attributeName)
                                    .concat("' type '").concat(attributeType).concat("'"));
                        }
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.gt.name:
                    {
                        operator.append(Constant.Operator.gt.symbol);
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.gte.name:
                    {
                        operator.append(Constant.Operator.gte.symbol);
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.lt.name:
                    {
                        operator.append(Constant.Operator.lt.symbol);
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.lte.name:
                    {
                        operator.append(Constant.Operator.lte.symbol);
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.neq.name:
                    {
                        operator.append(Constant.Operator.neq.symbol);
                        value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                        break;
                    }
                    case Constant.Operator.in.name:
                    {
                        operator.append(Constant.Operator.in.symbol);
                        StringBuffer buffer = new StringBuffer(OPEN_PARENTHESES);
                        dataAttribute.getJSONArray(op).forEach(elemn -> {
                            buffer.append(Type.getInstance().getValueFormattedByType(elemn, attributeType)).append(", ");
                        });
                        value = buffer.delete(buffer.length() - 2, buffer.length()).append(CLOSE_PARENTHESES).toString();
                        break;
                    }
                    default:
                        throw new RuntimeException("400:operator '".concat(op).concat("' unknow."));
                }
            } 
            else if (predicates.size() > 1)
            {
                innerConnective = dataAttribute.getString("CONNECTIVE");

                predicates.remove("CONNECTIVE");

                for (String op : predicates)
                {
                    switch (op)
                    {
                        case Constant.Operator.gt.name:
                            operator.append(Constant.Operator.gt.symbol);
                            value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                            break;

                        case Constant.Operator.gte.name:
                            operator.append(Constant.Operator.gte.symbol);
                            value = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                            break;

                        case Constant.Operator.lt.name:
                            flag = 1;
                            supValue = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                            break;

                        case Constant.Operator.lte.name:
                            flag = 2;
                            supValue = Type.getInstance().getValueFormattedByType(dataAttribute.get(op), attributeType);
                            break;

                        default:
                            throw new RuntimeException("400:operator '".concat(op).concat("' unknow."));
                    }
                }
            }
            else
            {
                throw new Exception("400:no operator found.");
            }
        }
        else if (!data.get(attributeName).toString().trim().equals(""))
        {
            value = Type.getInstance().getValueFormattedByType(data.get(attributeName), attributeType);

            if (value == null)
            {
                operator.append(_IS_);
            }
            else
            {
                operator.append(_EQUALS_);
            }
        }

        if (flag > 0)
        {
            whereClausula.add(new StringBuffer().append(OPEN_PARENTHESES)
                    .append(entityNameAlias).append(DOT).append(attributeName)
                            .append(operator.toString()).append(value)
                    .append(WHITESPACE).append(innerConnective).append(WHITESPACE)
                    .append(entityNameAlias).append(DOT).append(attributeName)
                            .append(flag == 1 ? Constant.Operator.lt.symbol : Constant.Operator.lte.symbol).append(supValue)
                .append(CLOSE_PARENTHESES).append(WHITESPACE).append(connective).append(WHITESPACE).toString());
        }
        else if (operator.length() > 0)
        {
            whereClausula.add(new StringBuffer()
                    /*.append(entityNameAlias).append(DOT)*/.append(attributeName).append(WHITESPACE)
                            .append(operator.toString()).append(WHITESPACE).append(value)
                    .append(WHITESPACE).append(connective).append(WHITESPACE).toString());
        }
    }
}
