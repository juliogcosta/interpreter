package com.yc.loco3.transformers.orm;

public interface SQLStatementTemplate
{
    final static public String CHECK_UNIQUE_CONSTRAINT_EXISTENCE = "SELECT constraint_schema, constraint_name FROM information_schema.table_constraints "
            .concat("WHERE constraint_schema = '{schema_name}' AND constraint_name = '{schema_name}_{table_name}_{key_name}_uk'");

    final static public String ADD_UNIQUE_CONSTRAINT = 
              "IF NOT EXISTS (" + CHECK_UNIQUE_CONSTRAINT_EXISTENCE + ") THEN \n"
            + " ALTER TABLE {schema_name}.{table_name} ADD CONSTRAINT {schema_name}_{table_name}_{key_name}_uk UNIQUE ({keys}); \n"
            + "END IF;";

    final static public String DROP_UNIQUE_CONSTRAINT = 
              "IF EXISTS (" + CHECK_UNIQUE_CONSTRAINT_EXISTENCE + ") THEN \n"
            + " ALTER TABLE {schema_name}.{table_name} DROP CONSTRAINT {schema_name}_{table_name}_{key_name}_uk; \n"
            + "END IF;";

    final static public String CHECK_UNIQUE_CK_CONSTRAINT_EXISTENCE = "SELECT constraint_schema, constraint_name FROM information_schema.table_constraints "
            .concat("WHERE constraint_schema = '{schema_name}' AND constraint_name = '{schema_name}_{table_name}_{key_name}_ck'");

    final static public String ADD_UNIQUE_CK_CONSTRAINT = 
              "IF NOT EXISTS (" + CHECK_UNIQUE_CK_CONSTRAINT_EXISTENCE + ") THEN \n"
            + " ALTER TABLE {schema_name}.{table_name} ADD CONSTRAINT {schema_name}_{table_name}_{key_name}_ck UNIQUE ({keys}); \n"
            + "END IF;";

    final static public String ADD_FK_CONSTRAINT = 
              "IF NOT EXISTS \n"
            + " (SELECT constraint_schema, constraint_name FROM information_schema.table_constraints"
            + " WHERE constraint_schema = '{schema_name}' AND constraint_name = '{schema_name}_{table_name}_{referenced_table}_{referenced_key}_fk') \n" 
            + "THEN \n"
            + " ALTER TABLE {schema_name}.{table_name} ADD CONSTRAINT {schema_name}_{table_name}_{referenced_table}_{referenced_key}_fk" 
            + "  FOREIGN KEY ({key_name})"
            + "  REFERENCES {schema_name}.{referenced_table} ({referenced_key}) ON UPDATE {ON_UPDATE} ON DELETE {ON_DELETE}; \n"
            + "END IF;";

    final static public String CHECK_INDEX_EXISTENCE = "SELECT indexname, indexdef FROM pg_indexes WHERE schemaname = '{schema_name}' and tablename = '{table_name}';";

    final static public String ADD_INDEXES_CONSTRAINT = 
            "IF NOT EXISTS (".concat(CHECK_INDEX_EXISTENCE).concat(") THEN ")
                .concat("CREATE INDEX {schema_name}_{table_name}_{key_name}_idx ON {schema_name}.{table_name} ({keys}); ")
            .concat("END IF;");
}