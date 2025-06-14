package com.yc.loco3.transformers.onrm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.yc.loco3.Constant;

public class NoSQLStatementsTransformerImpl implements NoSQLStatementsTransformer
{
    @Override
    public JSONObject reconfigureOOStts(final JSONObject model) throws Exception
    {
        final JSONObject newEntities = new JSONObject();

        model.keySet().forEach(entitySpecName -> 
        {
            newEntities.put(entitySpecName, model.getJSONObject(entitySpecName));
        });

        final JSONObject reconfiguredModel = new JSONObject();
        final Set<String> entitiesNames = new HashSet<String>(newEntities.keySet());

        entitiesNames.forEach(entityName -> 
        {
            reconfiguredModel.put(entityName, new JSONObject());
            reconfiguredModel.getJSONObject(entityName).put(Constant._conf,
                    new JSONObject(newEntities.getJSONObject(entityName).remove(Constant._conf).toString()));
        });

        newEntities.keySet().forEach(entityName -> 
        {
            newEntities.getJSONObject(entityName).getJSONArray(Constant.columns).forEach(elemn -> {
                JSONObject attribute = (JSONObject) elemn;
                reconfiguredModel.getJSONObject(entityName).put(attribute.getString(Constant.name),new JSONObject(attribute.toString()));
            });
        });

        return reconfiguredModel;
    }

    @Override
    public JSONObject getSttsFromOOStts(JSONObject entities) throws JSONException, Exception
    {
        final JSONObject statements = new JSONObject();

        final String noSQLInsertStatement = "INSERT INTO ".concat(":entityName: (:attributes:) VALUES ");
        final String noSQLUpdateStatement = "UPDATE ".concat(":entityName: SET :attribute=value: WHERE ");
        final String noSQLDeleteStatement = "DELETE FROM ".concat(":entityName: WHERE ");

        entities.keys().forEachRemaining(entityName -> 
        {
            statements.put(entityName, new JSONObject());

            try
            {
                final StringBuffer sqlSelectClausula = new StringBuffer();
                final StringBuffer sqlFromClausula = new StringBuffer();

                statements.getJSONObject(entityName).put(SELECT, new JSONObject());

                /*
                 * IMPORTANTE: Chamdas a 'getSQLStatementSelectBy' devem ser posterior a chamdas a 'getSQLStatementCreateTableBy'
                 * 
                 */
                final Set<String> entitiesNames = new HashSet<String>();
                entitiesNames.add(entityName);

                this.getSQLStatementSelectBy(entityName, entities, sqlSelectClausula, sqlFromClausula);

                statements.getJSONObject(entityName)
                        .put(SELECT, "SELECT "
                                .concat(sqlSelectClausula.substring(0, sqlSelectClausula.length() - 2).toString())
                                .concat(" FROM ").concat(sqlFromClausula.toString().trim()));

                statements.getJSONObject(entityName).put(COUNT,
                        "SELECT count(".concat(Constant.id).concat(") FROM ").concat(sqlFromClausula.toString().trim()));

                sqlSelectClausula.delete(0, sqlSelectClausula.length());
                sqlFromClausula.delete(0, sqlFromClausula.length());
                entitiesNames.clear();

                statements.getJSONObject(entityName).put(INSERT, noSQLInsertStatement.toString().replace(":entityName:", entityName));

                statements.getJSONObject(entityName).put(UPDATE, noSQLUpdateStatement.toString().replace(":entityName:", entityName));

                statements.getJSONObject(entityName).put(DELETE, noSQLDeleteStatement.toString().replace(":entityName:", entityName));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        });

        entities.keys().forEachRemaining(entityName -> {
            final StringBuffer attributesEntityModelToUpdate = new StringBuffer();
            final StringBuffer attributesEntityModelToInsert = new StringBuffer();
            final StringBuffer values = new StringBuffer();

            int index = 1;
            final JSONObject entityModel = entities.getJSONObject(entityName);
            final Iterator<String> attributeEntityModelIterator = entityModel.keys();
            while (attributeEntityModelIterator.hasNext())
            {
                final String attributeEntityModelName = attributeEntityModelIterator.next().toString().trim();
                if (!(attributeEntityModelName.equalsIgnoreCase(Constant._conf)
                        || attributeEntityModelName.equalsIgnoreCase(Constant.id)))
                {
                    entityModel.getJSONObject(attributeEntityModelName).put(Constant.sqlType,
                            Type.getInstance().getCQLTypeId(
                                    entityModel.getJSONObject(attributeEntityModelName).getString(Constant.type)));
                    entityModel.getJSONObject(attributeEntityModelName).put(Constant.sqlIndex, index);

                    attributesEntityModelToInsert.append(attributeEntityModelName).append(", ");
                    attributesEntityModelToUpdate.append(attributeEntityModelName).append("=?, ");

                    values.append("?, ");

                    index++;
                }
            }

            statements.getJSONObject(entityName)
                    .put(INSERT_, noSQLInsertStatement.toString().replace(":entityName:", entityName)
                            .replace(":attributes:",
                                    attributesEntityModelToInsert.delete(attributesEntityModelToInsert.length() - 2,
                                            attributesEntityModelToInsert.length()).toString())
                            .concat("(").concat(values.delete(values.length() - 2, values.length()).toString()).concat(")"));

            statements.getJSONObject(entityName)
                    .put(UPDATE_, noSQLUpdateStatement.toString().replace(":entityName:", entityName).replace(
                                    ":attribute=value:",
                                    attributesEntityModelToUpdate.delete(attributesEntityModelToUpdate.length() - 2,
                                            attributesEntityModelToUpdate.length()).toString()));
        });;

        return statements;
    }

    private void getSQLStatementSelectBy(String entityName, JSONObject entitiesSpecs, StringBuffer sqlSelectClausula,
            StringBuffer sqlFromClausula)
            throws JSONException, JSONException, Exception
    {
        entityName = entityName.trim();

        sqlFromClausula.append(entityName);

        final Iterator<String> attributeIterator = entitiesSpecs.getJSONObject(entityName).keys();
        while (attributeIterator.hasNext())
        {
            final String attributeName = attributeIterator.next().toString().trim();
            if (attributeName.equals(Constant._conf))
            {

            }
            else
            {
                sqlSelectClausula.append(attributeName).append(", ");
            }
        }
    }
}
