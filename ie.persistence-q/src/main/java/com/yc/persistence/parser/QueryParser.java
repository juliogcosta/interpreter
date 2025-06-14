package com.yc.persistence.parser;

import org.json.JSONArray;
import org.json.JSONObject;

public class QueryParser
{
    private int index = 1;

    static final public JSONObject query = new JSONObject(
              "{"
            + "  \"sexo\":\"F\","
            + "  \"turmas\":[{"
            + "    \"ref\":\"5A\","
            + "    \"professors\":[{"
            + "      \"nome\":\"joca\","
            + "      \"cursos\":[{"
            + "        \"nome\":\"matematica\""
            + "      }],"
            + "      \"documentos\":[{"
            + "        \"numero\":\"2.0\""
            + "      }]"
            + "    }]"
            + "  }],"
            + "  \"livros\":[{"
            + "    \"nome\":\"metamorfose\","
            + "    \"professors\":[{"
            + "      \"nome\":\"jose\","
            + "      \"cursos\":[{"
            + "        \"nome\":\"geografia\""
            + "      }]"
            + "    }]"
            + "  }]"
            + "}");

    /*
    static final public JSONObject query = new JSONObject(
              "{"
            + "  \"turmas\":[{"
            + "    \"nome\":\"5A\""
            + "  }],"
            + "  \"nome\":\"jose\""
            + "}");

     */

    static final public String Query = "{\"turmas\": [{\"nome\": \"s1-1\"}]}";

    public JSONArray parse(JSONObject sourceQuery)
    {
        JSONArray targetQueries = new JSONArray();

        JSONArray path = new JSONArray();

        JSONObject branchs = new JSONObject();

        JSONObject entities = new JSONObject();

        String rootEntityName = sourceQuery.keys().next();

        /*
        System.out.println(">>>>>>>>> 0.1: "+getClass()+": sourceQuery.getJSONObject("+rootEntityName+"): "
                +sourceQuery.getJSONObject(rootEntityName).toString(2));
         */

        run(rootEntityName, 
                rootEntityName, 
                sourceQuery.getJSONObject(rootEntityName), 
                new JSONObject(sourceQuery.getJSONObject(rootEntityName).toString()), 
                0, 
                path, 
                0, 
                branchs, 
                entities);

        /*
        System.out.println(">>>>>>>>> 0.2: "+getClass()+": branchs: "+branchs.toString(2));

        System.out.println(">>>>>>>>> 0.3: "+getClass()+": entities: "+entities.toString(2));
         */

        branchs.put(String.valueOf(branchs.length()), path.toString());

        for (String key:branchs.keySet())
        {
            JSONArray reversedBranch = new JSONArray();

            JSONArray branch = new JSONArray(branchs.getString(key));

            /*
            System.out.println(">>>>>>>>> 0.4: "+getClass()+": branchs.getString("+key+"): "+branchs.getString(key));

            System.out.println(">>>>>>>>> 0.5: "+getClass()+": branch: "+branch.toString(2));
             */

            for (int index = branch.length(); index > 0; index--)
            {
                reversedBranch.put(branch.getString(index - 1));
            }

            /*
            System.out.println(">>>>>>>>> 0.6: "+getClass()+": reversedBranch: "+reversedBranch.toString(2));
             */

            reversedBranch.put(rootEntityName);

            JSONObject query = new JSONObject();

            build(entities, reversedBranch, 0, query);

            targetQueries.put(query);
        }

        return targetQueries;
    }

    public void run(String root, String rRoot, JSONObject tree, JSONObject nTree, int level, JSONArray path, int branch, JSONObject branchs, JSONObject entities)
    {
        entities.put(rRoot, new JSONObject());

        for (String leaf:tree.keySet()) 
        {
            if (tree.get(leaf) instanceof JSONArray)
            {
                String l = leaf.substring(0, leaf.length() - 1).concat(".").concat(String.valueOf(index));

                nTree.put(l, nTree.remove(leaf));

                index++;

                if (level > branch)
                {
                    path.put(branch = level, l);
                }
                else
                {
                    if (path.length() > 0) 
                    {
                        branchs.put(String.valueOf(branchs.length()), path.toString());
                    }

                    for (int index = level; index < path.length(); index++)
                    {
                        path.remove(index);
                    }

                    path.put(level, l);
                }

                level++;

                run(leaf, l, tree.getJSONArray(leaf).getJSONObject(0), nTree.getJSONArray(l).getJSONObject(0), level, path, branch, branchs, entities);

                level--;
            }
            else
            {
                entities.getJSONObject(rRoot).put(leaf, nTree.get(leaf));
            }
        }
    }

    public void build(JSONObject entities, JSONArray branch, int pointer, JSONObject query)
    {
        if (pointer < branch.length())
        {
            String auxiliar = branch.getString(pointer);

            if (auxiliar.contains(".")) auxiliar = auxiliar.substring(0, auxiliar.indexOf("."));

            JSONObject entity = entities.getJSONObject(branch.getString(pointer));

                query.put(auxiliar, entity);

                build(entities, branch, ++pointer, query.getJSONObject(auxiliar));
            
        }
    }
}
