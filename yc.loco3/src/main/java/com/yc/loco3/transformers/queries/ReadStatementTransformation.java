package com.yc.loco3.transformers.queries;

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
import com.yc.loco3.transformers.orm.Type.YC;

public class ReadStatementTransformation extends StatementTransformation
{
    final Logger logger = LoggerFactory.getLogger(getClass());

    static public String UNDERLINE = "_";

    static public String WHITESPACE = " ";

    static public String OPEN_PARENTHESES = "(";

    static public String CLOSE_PARENTHESES = ")";

    static public String DOT = ".";

    static public String COMMA = ",";

    static public String _WHERE_ = " WHERE ";

    static public String _EQUALS_ = " = ";

    static public String _AS_ = " AS ";

    static public String _IS_ = " IS ";

    static public String _AND_ = " AND ";

    static public String _OR_ = " OR ";

    static public String ORDER_BY_ = "ORDER BY ";

    static public String _OFFSET_ = " OFFSET ";

    static public String LIMIT_ = "LIMIT ";

    static public String SELECT_ = "SELECT ";

    static public String _FROM_ = " FROM ";

    static public String _LEFT_JOIN_ = " LEFT JOIN ";

    static public String _ON_ = " ON ";

    //static private String AS_JSON_FUNCTION = "json_agg(to_json((SELECT d FROM (SELECT <cols>) d <order> <paging>))) AS data";
    //static private String STATEMENT_TEMPLATE = "select json_agg(to_json((select d from (1) d))) as data from (0) t";
    //static private String STATEMENT_TEMPLATE = "SELECT json_agg(to_json((SELECT d FROM (<1>) d))) AS data FROM (<0>) t";
    static private String STATEMENT_FPART_TEMPLATE = "SELECT json_agg(to_json((SELECT d FROM (";
    static private String STATEMENT_SPART_TEMPLATE = ") d))) AS data FROM (";
    static private String STATEMENT_TPART_TEMPLATE = ") ";

    static public int JOIN_ENTITY = 0;

    static public String DISTINCT_ = "DISTINCT ";

    static public String DISTINCT_AUX = "DISTINCT (";

    public ReadStatementTransformation(SchemaService schemaService)
    {
        super(schemaService);
    }

    public Helper getReadRelationalStatement(String tenantId, String entityName, JSONObject jsObject, CredentialServiceService userAuthorizationService, Set<String> auths) throws Exception
    {
        //logger.info(" > b error: 0.1.0");
        
        final Boolean count = jsObject.getBoolean(Constant._count);
        jsObject.remove(Constant._count);

        final JSONObject paging = jsObject.getJSONObject(Constant._paging);
        jsObject.remove(Constant._paging);

        final JSONObject sorting = jsObject.getJSONObject(Constant._sorting);
        jsObject.remove(Constant._sorting);

        final String connective = jsObject.getString(Constant._connective);
        jsObject.remove(Constant._connective);

        //logger.info(" > b error: 0.1.1");
        
        //logger.info(" > entityName: "+entityName);
        //logger.info(" > jsObject: "+jsObject.toString());
        final JSONObject data = jsObject.getJSONObject(entityName);
        //logger.info(" > getReadRelationalStatement(): data: "+data.toString());
        /*if (data.keySet().size() == 0)
        {
            throw new Exception("400:no highlighted attribute(s) for data retrieval.");
        }*/

        //logger.info(" > b error: 0.1.2");
        
        /*
        if (!data.has(Constant.id))
        {
            data.put(Constant.id, "");
        }
         */

        final List<String> toFilter = new ArrayList<String>();

        final StringBuffer selectClausula = new StringBuffer();
        final StringBuffer altSelectClausula = new StringBuffer();

        //logger.info(" > b error: 0.1.3");
        
        final StringBuffer fromClausula = new StringBuffer()
                .append("\"").append(super.schemaService.getSchemaName(tenantId)).append("\"").append(DOT).append(entityName).append(WHITESPACE).append("\"").append(entityName).append("\"");

        //logger.info(" > b error: 0.1.4");
        
        //logger.info(" > tenantId: "+tenantId);
        final JSONObject schema = super.schemaService.getOOStatements(tenantId);
        //logger.info(" > schema: "+schema);

        //logger.info(" > b error: 0.1.5");
        
        final Helper helper = new Helper();
        helper.schema = schema;
        helper.simpleQuery = true;

        //logger.info(" > b error: 0.1.6");
        
        this.configSQLQuery(
                super.schemaService.getSchemaName(tenantId),
                schema,
                entityName, 
                null, 
                data, 
                connective, 
                selectClausula, 
                altSelectClausula, 
                fromClausula, 
                toFilter, 
                userAuthorizationService,
                helper,
                auths);

        //logger.info(" > b error: 0.1.7");
        
        StringBuffer whereClausula = new StringBuffer();
        if (toFilter.size() > 0)
        {
            toFilter.forEach(item -> whereClausula.append(item));

            //logger.info(" > b error: 0.1.7.0");
            
            if (connective.equals("OR"))
            {
                //logger.info(" > b error: 0.1.7.0.0");
                
                whereClausula.delete(whereClausula.length() - 4, whereClausula.length());
            }
            else
            {
                //logger.info(" > b error: 0.1.7.0.1");
                
                whereClausula.delete(whereClausula.length() - 5, whereClausula.length());
            }
        }

        //logger.info(" > b error: 0.1.8");
        
        String orderClausula = "";
        String pagingClausula = "";

        if (count)
        {
            selectClausula.delete(0, selectClausula.length());
            selectClausula.append("count(*)");
        }
        else
        {
            //logger.info(" > b error: 0.1.9");
            
            selectClausula.delete(selectClausula.length() - 2, selectClausula.length());
            altSelectClausula.delete(altSelectClausula.length() - 2, altSelectClausula.length());

            //logger.info(" > sorting: "+sorting.toString(2));

            //logger.info(" > b error: 0.1.10");
            
            if (sorting.length() > 0)
            {
                //logger.info(" > *sorting: "+sorting.toString(2));
                
                JSONObject _conf = schema.getJSONObject(entityName).getJSONObject(Constant._conf);

                //logger.info(" > b error: 0.1.10.0");
                    
                if (sorting.has("0")) 
                {
                    //logger.info(" > b error: 0.1.10.0.0");
                    
                    String orderBy = sorting.getJSONObject("0").getString(Constant._orderBy);
                    if (schema.getJSONObject(entityName).getJSONObject(Constant._conf).has(Constant.partition))
                    {
                        orderBy = _conf.getJSONObject(Constant.partition).getString(Constant.by);
                    }

                    //logger.info(" > b error: 0.1.10.0.1");
                        
                    orderClausula = ORDER_BY_.concat("\"").concat(entityName.toLowerCase()).concat("\"").concat(DOT)
                            .concat(orderBy).concat(WHITESPACE).concat(sorting.getJSONObject("0").getString(Constant._order));

                    ///logger.info(" > orderClausula: "+orderClausula);

                    //logger.info(" > b error: 0.1.10.0.2");
                        
                    if (sorting.has("1")) 
                    {
                        //logger.info(" > b error: 0.1.10.0.2.0");
                        
                        orderBy = sorting.getJSONObject("1").getString(Constant._orderBy);
                        if (schema.getJSONObject(entityName).getJSONObject(Constant._conf).has(Constant.partition))
                        {
                            orderBy = _conf.getJSONObject(Constant.partition).getString(Constant.by);
                        }

                        orderClausula = orderClausula + ", \"".concat(entityName.toLowerCase()).concat("\"").concat(DOT)
                                .concat(orderBy).concat(WHITESPACE).concat(sorting.getJSONObject("1").getString(Constant._order));

                        //logger.info(" > orderClausula: "+orderClausula);
                        
                        if (sorting.has("2")) 
                        {
                            orderBy = sorting.getJSONObject("2").getString(Constant._orderBy);
                            if (schema.getJSONObject(entityName).getJSONObject(Constant._conf).has(Constant.partition))
                            {
                                orderBy = _conf.getJSONObject(Constant.partition).getString(Constant.by);
                            }

                            orderClausula = orderClausula + ", \"".concat(entityName.toLowerCase()).concat("\"").concat(DOT)
                                    .concat(orderBy).concat(WHITESPACE).concat(sorting.getJSONObject("2").getString(Constant._order));

                            //logger.info(" > orderClausula: "+orderClausula);
                        }
                    }
                    //logger.info(" > b error: 0.1.10.0.3");
                }
            }

            if (paging.length() > 0) 
            {
                pagingClausula = LIMIT_.concat(String.valueOf(paging.getInt(Constant._maxRegisters))).concat(_OFFSET_)
                        .concat(String.valueOf(paging.getInt(Constant._firstRegister)));
            }
        }

        //logger.info(" > helper.simpleQuery: "+helper.simpleQuery);
        if (helper.simpleQuery)
        {
            /*STATEMENT_FPART_TEMPLATE = "SELECT json_agg(to_json((SELECT d FROM ";
              STATEMENT_SPART_TEMPLATE = ") d))) AS data FROM (";
              STATEMENT_TPART_TEMPLATE = ") t";*/
            /*
            SELECT json_agg(to_json((SELECT d FROM (SELECT ord.number AS number, ord.notes AS notes, ord.id AS id) d))) AS data FROM (SELECT orde.number AS number, orde.notes AS notes, orde.id AS id FROM fexpfive.order orde ORDER BY orde.id ASC LIMIT 1000 OFFSET 0) ord
             * */

            helper.query = STATEMENT_FPART_TEMPLATE
                    .concat(SELECT_).concat(altSelectClausula.toString())
                .concat(STATEMENT_SPART_TEMPLATE)
                .concat(SELECT_.concat(altSelectClausula
                    .append(_FROM_).append(fromClausula.toString())
                    .append((whereClausula.length() > 0 ? 
                            _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) 
                            : WHITESPACE))
                    .append(orderClausula).append(WHITESPACE)
                    .append(pagingClausula).toString()))
                .concat(STATEMENT_TPART_TEMPLATE).concat("\"").concat(entityName).concat("\"");

            /*helper.query = SELECT_.concat(altSelectClausula
                .append(_FROM_).append(fromClausula.toString())
                .append((whereClausula.length() > 0 ? 
                        _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) 
                        : WHITESPACE))
                .append(orderClausula).append(WHITESPACE)
                .append(pagingClausula).toString());*/

            /*helper.query = SELECT_.concat(new StringBuffer()
                .append(AS_JSON_FUNCTION
                    .replace("<cols>", altSelectClausula.toString())
                    .replace("<order>", orderClausula.toString())
                    .replace("<paging>", pagingClausula.toString()))
                .append(_FROM_).append(fromClausula.toString())
                .append((whereClausula.length() > 0 ? 
                        _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) 
                        : WHITESPACE)).toString());*/
        }
        else
        {
            helper.query = SELECT_.concat(selectClausula
                .append(_FROM_).append(fromClausula.toString())
                .append((whereClausula.length() > 0 ? 
                        _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) 
                        : WHITESPACE))
                .append(orderClausula).append(WHITESPACE)
                .append(pagingClausula).toString());
        }

        //logger.info(" > helper: "+helper.query);

        return helper;
    }

    private void configSQLQuery(
            final String schemaName,
            final JSONObject schema,
            final String entityName, 
            final String entityNameAlias, 
            final JSONObject data, 
            final String connective, 
            final StringBuffer selectClausula, 
            final StringBuffer altSelectClausula, 
            final StringBuffer fromClausula, 
            final List<String> whereClausula, 
            final CredentialServiceService userAuthorizationService,
            final Helper helper,
            final Set<String> auths) throws Exception
    {
        //logger.info(" > configSQLQuery(): data: "+data.toString());
        //logger.info(" > schema: "+schema.toString(2));
        final JSONObject entity = schema.getJSONObject(entityName);
        helper.dbType = entity.getJSONObject(Constant._conf).getString("dbType");
        //logger.info(" > entity: "+entity.toString(2));

        //userAuthorizationService.check(entity.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read));
        userAuthorizationService.check(entity.getJSONObject(Constant._conf).getJSONObject(Constant.accessControl).getJSONArray(Constant.read), auths);
        

        try
        {
            final String aliasForEntityName = entityNameAlias == null ? entityName : entityNameAlias;

            final JSONObject associations = entity.getJSONObject(Constant.associations);
            final JSONObject attributes = entity.getJSONObject(Constant.attributes);
            final JSONObject components = entity.getJSONObject(Constant.components);

            /*if (!data.has(Constant.id) && !entity.getJSONObject(Constant._conf).has(Constant.partition))
            {
                data.put(Constant.id, "");
            }*/

            //logger.info(" > attributes  : "+attributes.toString());

            final JSONObject hasDistinct = new JSONObject();

            for (String attributeName : attributes.keySet()) 
            {
                if (!hasDistinct.has(Constant.Operator.distinct.name))
                {
                    //logger.info(" > attributeName: "+attributeName.toString());

                    ReadStatementTransformation.this.extractPredicate(
                            entityName, 
                            aliasForEntityName, 
                            attributeName, 
                            attributes, 
                            data, 
                            connective, 
                            selectClausula, 
                            altSelectClausula, 
                            whereClausula, 
                            hasDistinct,
                            helper);
                }
                /*if (data.has(attributeName))
                {
                    if (!hasDistinct.has(Constant.Operator.distinct.name))
                    {
                        try
                        {
                            ReadStatementTransformation.this.extractPredicate(
                                        entityName, 
                                        aliasForEntityName, 
                                        attributeName, 
                                        attributes, 
                                        data, 
                                        connective, 
                                        selectClausula, 
                                        altSelectClausula, 
                                        whereClausula, 
                                        hasDistinct,
                                        helper);
                        }
                        catch (Exception e)
                        {
                                throw new RuntimeException(e);
                        }
                    }
                }
                else
                {
                    selectClausula.append("\"").append(aliasForEntityName).append("\"").append(DOT).append(attributeName)
                        .append(_AS_).append(aliasForEntityName).append(UNDERLINE).append(attributeName).append(COMMA).append(WHITESPACE);

                    altSelectClausula.append("\"").append(aliasForEntityName).append("\"").append(DOT).append(attributeName)
                        .append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);
                }*/
            }

            for (String associationName : associations.keySet()) 
            {
                //logger.info(" > associationName: "+associationName);

                //logger.info(" > data.has("+associationName+"): "+data.has(associationName));

                if (data.has(associationName)) 
                {
                    //logger.info(" > hasDistinct.has(Constant.Operator.distinct.name): "+hasDistinct.has(Constant.Operator.distinct.name));

                    if (!hasDistinct.has(Constant.Operator.distinct.name))
                    {
                        final String associationEntityName = associations.getJSONObject(associationName).getString(Constant.type);

                        fromClausula
                            .append(_LEFT_JOIN_).append("\"").append(schemaName).append("\"").append(DOT).append(associationEntityName).append(WHITESPACE).append("\"").append(associationName).append("\"")
                                    .append(_ON_).append("\"").append(entityName).append("\"").append(DOT).append(associationName).append(_EQUALS_).append("\"").append(associationName).append("\"").append(DOT).append(Constant.id);

                        JSONObject assoc;
                        if (data.get(associationName) instanceof JSONObject)
                        {
                            assoc = data.getJSONObject(associationName);
                        }
                        else
                        {
                            assoc = new JSONObject();
                            assoc.put(Constant.id, data.getLong(associationName));
                        }

                        helper.simpleQuery = false;

                        //logger.info(" > calling new configSQLQuery!");

                        this.configSQLQuery(schemaName,
                                schema, 
                                associationEntityName, 
                                associationName, 
                                data.getJSONObject(associationName), 
                                connective, 
                                selectClausula, 
                                altSelectClausula, 
                                fromClausula, 
                                whereClausula, 
                                userAuthorizationService, 
                                helper,
                                auths);
                    }
                }
            }

            for (String componentName : components.keySet()) 
            {
                if (!hasDistinct.has(Constant.Operator.distinct.name))
                {
                    helper.simpleQuery = false;

                    JSONObject dataComponent = data.getJSONObject(componentName);
                    JSONObject attributesComponent = components.getJSONObject(componentName).getJSONObject(Constant.attributes);

                    dataComponent.keySet().forEach(attributeNameComponent -> {
                        try
                        {
                            if (attributesComponent.has(attributeNameComponent)) 
                            {
                                ReadStatementTransformation.this.extractPredicate(
                                        entityName, 
                                        aliasForEntityName, 
                                        attributeNameComponent, 
                                        attributesComponent, 
                                        dataComponent, 
                                        connective, 
                                        selectClausula, 
                                        altSelectClausula, 
                                        whereClausula, 
                                        hasDistinct,
                                        helper);
                            }
                            else
                            {
                                throw new RuntimeException("400:unknown component '".concat(componentName).concat("' attribute '").concat(attributeNameComponent).concat("'."));
                            }
                        }
                        catch (Exception e)
                        {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    private void extractPredicate(String entityName, 
            String entityNameAlias, 
            String attributeName, 
            JSONObject attributes, 
            JSONObject data, 
            String connective, 
            StringBuffer selectClausula, 
            StringBuffer altSelectClausula, 
            List<String> whereClausula, 
            JSONObject hasDistinct,
            Helper helper) throws Exception
    {
        //logger.info(" > attributes.getJSONObject("+attributeName+"): "+attributes.getJSONObject(attributeName).toString(2));

        String attributeType = attributes.getJSONObject(attributeName).getString(Constant.type);

        if (attributeType.equals(YC.File)) return;

        StringBuffer operator = new StringBuffer();
        String innerConnective = _AND_;
        String value = null;
        Integer flag = 0;
        String supValue = null;

        //logger.info(" > configSQLQuery(): data: "+data.toString());

        if (!data.has(attributeName))
        {
            selectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                .append(_AS_).append(entityNameAlias).append(UNDERLINE).append(attributeName).append(COMMA).append(WHITESPACE);

            altSelectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                .append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);
        }
        else if (data.get(attributeName) instanceof JSONObject)
        {
            JSONObject dataAttribute = data.getJSONObject(attributeName);

            //logger.info(" > dataAttribute: "+dataAttribute.toString());

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
                    case Constant.Operator.distinct.name:
                    {
                        selectClausula.delete(0, selectClausula.length());
                        selectClausula.append(DISTINCT_).append(OPEN_PARENTHESES).append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName).append(CLOSE_PARENTHESES)
                                .append(_AS_).append(entityNameAlias).append(UNDERLINE).append(attributeName).append(COMMA).append(WHITESPACE);
                        hasDistinct.put(Constant.Operator.distinct.name, true);

                        altSelectClausula.delete(0, selectClausula.length());
                        altSelectClausula.append(DISTINCT_).append(OPEN_PARENTHESES).append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName).append(CLOSE_PARENTHESES)
                                .append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);

                        helper.simpleQuery = false;

                        break;
                    }
                    default:
                        throw new RuntimeException("400:operator '".concat(op).concat("' unknow."));
                }
            } 
            else if (predicates.size() > 1)
            {
                //logger.info(" > *predicates: "+predicates.toString());

                if (dataAttribute.has("CONNECTIVE"))
                {
                    innerConnective = dataAttribute.getString("CONNECTIVE");
                }
                else
                {
                    innerConnective = "AND";
                }

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
                
                //logger.info(" > *predicates: "+predicates.toString());
                //logger.info(" > *operator: "+operator.toString());
                //logger.info(" > *value: "+value.toString());
            }
            else
            {
                throw new Exception("400:no operator found.");
            }

            //if (helper.simpleQuery)
            {
                selectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                        .append(_AS_).append(entityNameAlias).append(UNDERLINE).append(attributeName).append(COMMA).append(WHITESPACE);

                altSelectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                        .append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);
            }
        }
        else if (!data.get(attributeName).toString().trim().equals(""))
        {
            value = Type.getInstance().getValueFormattedByType(data.get(attributeName), attributeType);

            //logger.info(" > value: "+value);
            if (value == null)
            {
                operator.append(_IS_);
            }
            else
            {
                if (value.startsWith("'%"))
                {
                    operator.append(Constant.Operator.ilike.symbol);
                }
                else if (value.endsWith("%'"))
                {
                    operator.append(Constant.Operator.ilike.symbol);
                }
                else operator.append(_EQUALS_);
            }

            selectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                    .append(_AS_).append(entityNameAlias).append(UNDERLINE).append(attributeName).append(COMMA).append(WHITESPACE);

            altSelectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                    .append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);
        }

        if (flag > 0)
        {
            //logger.info(" > (flag > 0): value: "+operator.toString()+" "+value);

            whereClausula.add(new StringBuffer().append(OPEN_PARENTHESES)
                    .append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                            .append(operator.toString()).append(value)
                    .append(WHITESPACE).append(innerConnective).append(WHITESPACE)
                    .append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                            .append(flag == 1 ? Constant.Operator.lt.symbol : Constant.Operator.lte.symbol).append(supValue)
                .append(CLOSE_PARENTHESES).append(WHITESPACE).append(connective).append(WHITESPACE).toString());
        }
        else if (operator.length() > 0)
        {
            //logger.info(" > (flag < 0 && operator.length() > 0): value: "+operator.toString()+" "+value);

            whereClausula.add(new StringBuffer()
                    .append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName).append(WHITESPACE)
                            .append(operator.toString()).append(WHITESPACE).append(value)
                    .append(WHITESPACE).append(connective).append(WHITESPACE).toString());
        }
    }
}
