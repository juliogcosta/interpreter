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

public class ReadStatementTransformationForExtractQueryModel extends StatementTransformation
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

    public ReadStatementTransformationForExtractQueryModel(SchemaService schemaService)
    {
        super(schemaService);
    }

    public Helper getReadRelationalStatement(String tenantId, String entityName, JSONObject jsObject, CredentialServiceService userAuthorizationService, Set<String> auths) throws Exception
    {
        final Boolean count = jsObject.getBoolean(Constant._count);
        jsObject.remove(Constant._count);

        final JSONObject paging = jsObject.getJSONObject(Constant._paging);
        jsObject.remove(Constant._paging);

        final JSONObject sorting = jsObject.getJSONObject(Constant._sorting);
        jsObject.remove(Constant._sorting);

        final String connective = jsObject.getString(Constant._connective);
        jsObject.remove(Constant._connective);

        final JSONObject data = jsObject.getJSONObject(entityName);
        /*if (data.keySet().size() == 0)
        {
            throw new Exception("400:no highlighted attribute(s) for data retrieval.");
        }*/

        /*
        if (!data.has(Constant.id))
        {
            data.put(Constant.id, "");
        }
         */

        final List<String> toFilter = new ArrayList<String>();

        final StringBuffer selectClausula = new StringBuffer();
        final StringBuffer altSelectClausula = new StringBuffer();

        final StringBuffer fromClausula = new StringBuffer()
                .append("\"").append(super.schemaService.getSchemaName(tenantId)).append("\"").append(DOT).append(entityName).append(WHITESPACE).append("\"").append(entityName).append("\"");

        //logger.info(" > tenantId: "+tenantId);
        final JSONObject schema = super.schemaService.getOOStatements(tenantId);
        //logger.info(" > schema: "+schema);

        final Helper helper = new Helper();
        helper.simpleQuery = true;

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
                auths,
                "");

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

        String orderClausula = "";
        String pagingClausula = "";

        if (count)
        {
            selectClausula.delete(0, selectClausula.length());
            selectClausula.append("count(*)");
        }
        else
        {
            selectClausula.delete(selectClausula.length() - 2, selectClausula.length());
            altSelectClausula.delete(altSelectClausula.length() - 2, altSelectClausula.length());

            if (sorting.length() > 0)
            {
                String orderBy = sorting.getString(Constant._orderBy);
                JSONObject _conf = schema.getJSONObject(entityName).getJSONObject(Constant._conf);
                if (schema.getJSONObject(entityName).getJSONObject(Constant._conf).has(Constant.partition))
                {
                    orderBy = _conf.getJSONObject(Constant.partition).getString(Constant.by);
                }

                orderClausula = ORDER_BY_.concat("\"").concat(entityName.toLowerCase()).concat("\"").concat(DOT)
                        .concat(orderBy).concat(WHITESPACE).concat(sorting.getString(Constant._order));
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

            //helper.clausula[Helper.Index.SELECT.ordinal()] = altSelectClausula.toString();
            //helper.clausula[Helper.Index.FROM.ordinal()] = fromClausula.toString();
            //helper.clausula[Helper.Index.WHERE.ordinal()] = (whereClausula.length() > 0 ? _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) : WHITESPACE);
            //helper.clausula[Helper.Index.ORDER.ordinal()] = orderClausula.toString();

            helper.clausula.put("ORDER", orderClausula.toString());
        }
        else
        {
            helper.query = SELECT_.concat(
                selectClausula.append(_FROM_).append(fromClausula.toString())
                .append((whereClausula.length() > 0 ?  _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) : WHITESPACE))
                .append(orderClausula).append(WHITESPACE)
                .append(pagingClausula).toString());

            //helper.clausula[Helper.Index.SELECT.ordinal()] = selectClausula.toString();
            //helper.clausula[Helper.Index.FROM.ordinal()] = fromClausula.toString();
            //helper.clausula[Helper.Index.WHERE.ordinal()] = (whereClausula.length() > 0 ? _WHERE_.concat(whereClausula.toString()).concat(WHITESPACE) : WHITESPACE);
            //helper.clausula[Helper.Index.ORDER.ordinal()] = orderClausula.toString();

            helper.clausula.put("ORDER", orderClausula.toString());
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
            final Set<String> auths,
            final String rootEntityNameAlias) throws Exception
    {
        //logger.info(" > schema: "+schema.toString(2));
        final JSONObject entity = schema.getJSONObject(entityName);

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

                    ReadStatementTransformationForExtractQueryModel.this.extractPredicate(
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
                            helper,
                            rootEntityNameAlias);
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
                                auths, 
                                rootEntityNameAlias.equals("") ? aliasForEntityName : rootEntityNameAlias.concat(".").concat(aliasForEntityName));
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
                        try {
                            if (attributesComponent.has(attributeNameComponent)) {
                                ReadStatementTransformationForExtractQueryModel.this.extractPredicate(
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
                                        helper,
                                        rootEntityNameAlias);
                                //rootEntityNameAlias.equals("") ? "" : rootEntityNameAlias.concat(".").concat(aliasForEntityName));
                            } else {
                                throw new RuntimeException("400:unknown component '".concat(componentName).concat("' attribute '").concat(attributeNameComponent).concat("'."));
                            }
                        } catch (Exception e) {
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

    private void extractPredicate(final String entityName, 
            final String entityNameAlias, 
            String attributeName, 
            JSONObject attributes, 
            JSONObject data, 
            String connective, 
            StringBuffer selectClausula, 
            StringBuffer altSelectClausula, 
            List<String> whereClausula, 
            JSONObject hasDistinct,
            Helper helper,
            String path) throws Exception
    {
        //logger.info(" > attributes.getJSONObject("+attributeName+"): "+attributes.getJSONObject(attributeName).toString(2));

        final String attributeType = attributes.getJSONObject(attributeName).getString(Constant.type);
        if (attributeType.equals(YC.File)) 
        {
            return;
        }

        final String attributeLength;
        if (attributeType.equals("String"))
        {
            //logger.info(" > attribute: "+attributes.getJSONObject(attributeName).toString(2));
            attributeLength = String.valueOf(attributes.getJSONObject(attributeName).getInt(Constant.length));
        }
        else attributeLength = null;

        StringBuffer operator = new StringBuffer();
        String innerConnective = _AND_;
        Integer flag = 0;
        Boolean hasQuote = false;

        if (helper.clausula.has("SELECT"))
        {

        }
        else 
        {
            helper.clausula.put("SELECT", new JSONObject());
            helper.clausula.put("WHERE", new JSONObject());
        }

        if (helper.clausula.getJSONObject("SELECT").has(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)))
        {

        }
        else 
        {
            helper.clausula.getJSONObject("SELECT").put(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias), new JSONObject());
        }

        if (helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).has("entityName"))
        {

        }
        else 
        {
            helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).put("entityName", entityName);
            helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).put("attributes", new JSONObject());
        }

        helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").put(attributeName, new JSONObject());
        helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).put("type", attributeType);
        helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).put("nullable", "true");
        helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).put("length", attributeLength == null ? null : attributeLength);
        helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).put("column", new JSONObject());

        if (!data.has(attributeName))
        {
            final String alias = entityNameAlias.concat(UNDERLINE).concat(attributeName);
            selectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName).append(_AS_).append(alias).append(COMMA).append(WHITESPACE);

            altSelectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName).append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);

            helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).getJSONObject("column").put("name", alias);
        }
        else if (data.get(attributeName) instanceof JSONObject)
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

                        break;
                    }
                    case Constant.Operator.gt.name:
                    {
                        operator.append(Constant.Operator.gt.symbol);

                        break;
                    }
                    case Constant.Operator.gte.name:
                    {
                        operator.append(Constant.Operator.gte.symbol);

                        break;
                    }
                    case Constant.Operator.lt.name:
                    {
                        operator.append(Constant.Operator.lt.symbol);

                        break;
                    }
                    case Constant.Operator.lte.name:
                    {
                        operator.append(Constant.Operator.lte.symbol);

                        break;
                    }
                    case Constant.Operator.neq.name:
                    {
                        operator.append(Constant.Operator.neq.symbol);

                        break;
                    }
                    case Constant.Operator.in.name:
                    {
                        operator.append(Constant.Operator.in.symbol);
                        StringBuffer buffer = new StringBuffer(OPEN_PARENTHESES);
                        dataAttribute.getJSONArray(op).forEach(elemn -> {
                            buffer.append(Type.getInstance().getValueFormattedByType(elemn, attributeType)).append(", ");
                        });

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

                            break;

                        case Constant.Operator.gte.name:
                            operator.append(Constant.Operator.gte.symbol);

                            break;

                        case Constant.Operator.lt.name:
                            flag = 1;

                            break;

                        case Constant.Operator.lte.name:
                            flag = 2;

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

            //if (helper.simpleQuery)
            {
                final String alias = entityNameAlias.concat(UNDERLINE).concat(attributeName);
                selectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                    .append(_AS_).append(alias).append(COMMA).append(WHITESPACE);

                altSelectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                        .append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);

                helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).getJSONObject("column").put("name", alias);
            }
        }
        else if (!data.get(attributeName).toString().trim().equals(""))
        {
            operator.append(_EQUALS_);

            final String alias = entityNameAlias.concat(UNDERLINE).concat(attributeName);
            selectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                    .append(_AS_).append(alias).append(COMMA).append(WHITESPACE);

            altSelectClausula.append("\"").append(entityNameAlias).append("\"").append(DOT).append(attributeName)
                    .append(_AS_).append(attributeName).append(COMMA).append(WHITESPACE);

            helper.clausula.getJSONObject("SELECT").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).getJSONObject("column").put("name", alias);
        }

        if (helper.clausula.getJSONObject("WHERE").has(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)))
        {

        }
        else 
        {
            helper.clausula.getJSONObject("WHERE").put(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias), new JSONObject());
            helper.clausula.getJSONObject("WHERE").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).put("attributes", new JSONObject());
        }

        final String alias = "[".concat(entityNameAlias).concat(":").concat(attributeName);

        if (attributeType.equals("String") || attributeType.equals("Date") || attributeType.equals("Timestamp"))
        {
            hasQuote = true;
        }

        if (flag > 0)
        {
            final String aliasMin = alias.concat(":min").concat("]");
            final String aliasMax = alias.concat(":max").concat("]");
            whereClausula.add(new StringBuffer().append(OPEN_PARENTHESES)
                    .append("\"").append(entityNameAlias).append("\"").append(UNDERLINE /* was DOT */).append(attributeName)
                            .append(operator.toString()).append(hasQuote ? " '" : "").append(aliasMin).append(hasQuote ? "'" : "")
                    .append(WHITESPACE).append(innerConnective).append(WHITESPACE)
                    .append("\"").append(entityNameAlias).append("\"").append(UNDERLINE /* was DOT */).append(attributeName)
                            .append(flag == 1 ? Constant.Operator.lt.symbol : Constant.Operator.lte.symbol).append(hasQuote ? " '" : "").append(aliasMax).append(hasQuote ? "'" : "")
                .append(CLOSE_PARENTHESES).append(WHITESPACE).append(connective).append(WHITESPACE).toString());

            helper.clausula.getJSONObject("WHERE").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).put("filter", new JSONObject());
            helper.clausula.getJSONObject("WHERE").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).getJSONObject("filter").put("range", new JSONObject());
            helper.clausula.getJSONObject("WHERE").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).getJSONObject("filter").getJSONObject("range").put("min", aliasMin);
            helper.clausula.getJSONObject("WHERE").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).getJSONObject("filter").getJSONObject("range").put("max", aliasMax);
            helper.clausula.getJSONObject("WHERE").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).put("connective", innerConnective);
        }
        else if (operator.length() > 0)
        {
            whereClausula.add(new StringBuffer()
                    .append("\"").append(entityNameAlias).append("\"").append(UNDERLINE /* was DOT */).append(attributeName).append(WHITESPACE).append(operator.toString()).append(WHITESPACE).append(hasQuote ? "'" : "").append(alias.concat("]")).append(hasQuote ? "'" : "")
                    .append(WHITESPACE).append(connective).append(WHITESPACE).toString());
            helper.clausula.getJSONObject("WHERE").getJSONObject(path.equals("") ? entityNameAlias : path.concat(".").concat(entityNameAlias)).getJSONObject("attributes").getJSONObject(attributeName).put("filter", alias.concat("]"));
        }
    }
}
