package com.yc.loco3.transformers;

import com.yc.loco3.schema.SQLStatementService;
import com.yc.loco3.schema.SchemaService;

public abstract class StatementTransformation
{
    protected SchemaService schemaService = null;
    protected SQLStatementService sqlStatementService = null;

    public StatementTransformation(SchemaService schemaService)
    {
        this.schemaService = schemaService;
    }
    
    /*
    protected void getAttributesSpecsForSpecializedForObjectSpec(JSONObject schema, JSONObject objectSpec, JSONObject attributesSpecs)
    {
        if (objectSpec.has(Constant._conf) && objectSpec.getJSONObject(Constant._conf).has(Constant.superEntity))
        {
            String superEntityName = objectSpec.getJSONObject(Constant._conf).getString(Constant.superEntity);
            JSONObject superObjectSpec = schema.getJSONObject(superEntityName);

            this.getAttributesSpecsForSpecializedForObjectSpec(schema, superObjectSpec, attributesSpecs);
        }

        objectSpec.keySet().parallelStream().forEach(attributeName -> {
            attributesSpecs.put(attributeName, objectSpec.getJSONObject(attributeName));
        });
    }

    protected JSONObject getEntitySpec(JSONObject schema, String entityName) throws Exception
    {
        final JSONObject objectSpec;
        final JSONObject tmpObjectSpec = schema.getJSONObject(entityName);
        if (tmpObjectSpec.getJSONObject(Constant._conf).has(Constant.superEntity))
        {
            String strategy = tmpObjectSpec.getJSONObject(Constant._conf).getString(Constant.superEntityStrategy);
            if (strategy.equals(Constant.TablePerEntity))
            {
                objectSpec = new JSONObject();

                this.getAttributesSpecsForSpecializedForObjectSpec(schema, tmpObjectSpec, objectSpec);

                return objectSpec;
            }
            else if (strategy.equals(Constant.JoinEntity))
            {
                return tmpObjectSpec;
            }
            else
            {
                throw new RuntimeException("400:unknow class specialization strategy.");
            }
        }
        else
        {
            return tmpObjectSpec;
        }
    }
     */
}
