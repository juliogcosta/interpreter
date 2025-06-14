package com.yc.loco3;

public interface Constant
{
    public interface Operator
    {
        public interface eq 
        {
            public String name = "eq";

            public String symbol = "=";
        }

        public interface lt 
        {
            public String name = "lt";

            public String symbol = "<";
        }

        public interface gt 
        {
            public String name = "gt";

            public String symbol = ">";
        }

        public interface lte 
        {
            public String name = "lte";

            public String symbol = "<=";
        }

        public interface gte 
        {
            public String name = "gte";

            public String symbol = ">=";
        }

        public interface neq 
        {
            public String name = "neq";

            public String symbol = "!=";
        }

        public interface isNotNull 
        {
            public String name = "isNotNull";

            public String symbol = "*";
        }

        public interface isNull 
        {
            public String name = "isNull";

            public String symbol = "n";
        }

        public interface in 
        {
            public String name = "in";

            public String symbol = "in";
        }

        public interface ilike 
        {
            public String name = "ilike";

            public String symbol = "ilike";
        }

        public interface like 
        {
            public String name = "like";

            public String symbol = "like";
        }

        public interface distinct 
        {
            public String name = "distinct";

            public String symbol = "distinct";
        }
    }

    enum action { CREATE, READ, UPDATE, DELETE };

    enum sqlAction { INSERT, SELECT, UPDATE, DELETE };

    public String X_TENANT_ID = "X-Tenant-Id";

    public String X_TENANT_SECRET = "X-Tenant-Secret";


    public String ooStts = "ooStts";

    public String relationalStts = "relationalStts";

    public String nonRelationalStts = "nonRelationalStts";


    public String enabled = "enabled";

    public String extension = "extension";

    public String adstracts = "abstracts";

    public String adstract = "abstract";

    public String entities = "entities";

    public String entity = "entity";

    public String source = "source";

    public String type = "type";

    public String Type = "Type";

    public String value = "value";

    public String sql = "sql";

    public String nosql = "nosql";

    public String instanceId = "instanceId";

    public String tenantId = "tenantId";

    public String sqlStatement = "sqlStatement";

    public String sqlStatementParams = "sqlStatementParams";

    public String _conf = "_conf";

    public String ForwardTo = "ForwardTo";

    public String content = "content";

    public String superEntity = "superEntity";

    public String entityName = "entityName";

    public String object = "object";

    public String partition = "partition";

    public String partitionedEntities = "partitions";

    public String _infos = "_infos";

    public String by = "by";

    public String ranges = "ranges";

    public String infLimit = "infLimit";

    public String supLimit = "supLimit";

    public String componentOf = "componentOf";

    public String enumerations = "enumerations";

    public String enumeration = "enumeration";

    public String components = "components";

    public String component = "component";

    public String references = "references";

    public String listOf = "listOf";

    public String superEntityStrategy = "superEntityStrategy";

    public String componentOfStrategy = "componentOfStrategy";

    public String strategy = "strategy";

    public String TablePerEntity = "TablePerEntity";

    public String JoinEntity = "JoinEntity";

    public String concurrencyControl = "concurrencyControl";

    public String accessControl = "accessControl";

    public String AccessControl = "AccessControl";

    public String write = "write";

    public String read = "read";

    public String businessRule = "businessRule";

    public String after = "after";

    public String call = "call";

    public String before = "before";

    public String uri = "uri";

    public String MASTER = "MASTER";

    public String status = "status";

    public String sqlIndex = "sqlIndex";

    public String sqlType = "sqlType";

    public String dbType = "dbType";

    public String uniqueKey = "uniqueKey";

    public String partitionKeys = "partitionKeys";

    public String clusteringColumns = "clusteringColumns";

    public String columns = "columns";

    public String name = "name";

    public String Name = "Name";

    public String columnName = "columnName";

    public String nullable = "nullable";

    public String unique = "unique";

    public String index = "index";

    public String persistent = "persistent";

    public String length = "length";

    public String attributes = "attributes";

    public String associations = "associations";

    public String comment = "comment";

    public String indexKey = "indexKey";

    public String files = "files";

    public String url = "url";

    public String URL = "URL";

    public String id = "id";

    public String view = "view";

    public String label = "label";

    public String text = "text";

    public String mask = "mask";

    public String textarea = "textarea";

    public String select = "select";

    public String upload = "upload";

    public String fileupload = "fileupload";

    public String options = "options";

    public String logrole = "logrole";

    public String loguser = "loguser";

    public String logversion = "logversion";

    public interface Mask 
    {
        public String alpha = "alpha";
        public String alpha_value = "{\"exp\": \"Z\",\"translation: {\"Z\": { \"pattern\": \"/[a-zA-Z]/\", \"recursive\": true }}}";

        public String alpha_ = "alpha_";
        public String alpha__value = "{\"exp\": \"Z\",\"translation: {\"Z\": { \"pattern\": \"/[a-zA-Z ]/\", \"recursive\": true }}}";

        public String numeric = "numeric";
        public String numeric_value = "{\"exp\": \"Z\",\"translation: {\"Z\": { \"pattern\": \"/[0-9]/\", \"recursive\": true }}}";

        public String numeric_ = "numeric_";
        public String numeric__value = "{\"exp\": \"Z\",\"translation: {\"Z\": { \"pattern\": \"/[0-9 ]/\", \"recursive\": true }}}";

        public String alphanumeric = "alphanumeric";
        public String alphanumeric_value = "{\"exp\": \"Z\",\"translation: {\"Z\": { \"pattern\": \"/[a-zA-Z0-9]/\", \"recursive\": true }}}";

        public String alphanumeric_ = "alphanumeric_";
        public String alphanumeric__value = "{\"exp\": \"Z\",\"translation: {\"Z\": { \"pattern\": \"/[a-zA-Z0-9 ]/\", \"recursive\": true }}}";

        public String email = "email";
        public String email_value = "{\"exp\": \"A\",\"translation: {\"A\": { \"pattern\": \"/[\\w@\\-.+]/\" }}}";

        public String phone_br = "phone_br";
        public String phone_br_value = "(00) 0 0000-0000";

        public String phone_us = "phone_us";
        public String phone_us_value = "";

        public String money_br = "money_br";
        public String money_br_value = "#.##0,00";

        public String money_us = "money_us";
        public String money_us_value = "#,##0.00";

        public String date_br = "date_br";
        public String date_br_value = "00/00/0000";

        public String date_us = "date_us";
        public String date_us_value = "";

        public String date_and_time_br = "date_and_time_br";
        public String date_and_time_br_value = "00/00/0000";

        public String date_and_time_us = "date_us";
        public String date_and_time_us_value = "00/00/0000";

        public String state_uf = "state_uf";
        public String state_uf_value = "AA";

        public String doc_CPF = "doc_CPF";
        public String doc_CPF_value = "000.000.000-00";

        public String doc_RG = "doc_RG";
        public String doc_RG_value = "0.000.000";
    }

    /*
    public String logcreatedat = "logcreatedat";

    public String logupdatedat = "logupdatedat";
     */

    public String createdat = "createdat";

    public String updatedat = "updatedat";

    public String where = "where";

    public String action = "action";

    public String data = "data";

    public String CREATE = "CREATE";

    public String create = "create";

    public String READ = "READ";

    public String UPDATE = "UPDATE";

    public String update = "update";

    public String DELETE = "DELETE";

    public String delete = "delete";

    public String count = "count";

    public String _count = "_count";

    public String _cache = "_cache";

    public String _ttl = "_ttl";

    public String _behavior = "_behavior";

    public String evict = "evict";

    public String ignore = "ignore";

    public String use = "use";

    public String totalRegisters = "totalRegisters";

    public String _orderBy = "_orderBy";

    public String _order = "_order";

    public String _maxRegisters = "_maxRegisters";

    public String _firstRegister = "_firstRegister";

    public String _paging = "_paging";

    public String _sorting = "_sorting";

    public String _as = "_as";

    public String _associations = "_associations";

    public String _connective = "_connective";

    public String _populating = "_populating";

    public String _level = "_level";


    public String UserIDToken = "UserIDToken";

    public String xTenantID = "xTenantID";


    public String queueName = "queueName";

    public String queue = "queue";

    public String message = "message";
}
