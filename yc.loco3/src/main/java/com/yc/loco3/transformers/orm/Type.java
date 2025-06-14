package com.yc.loco3.transformers.orm;

import java.sql.Types;
import java.util.regex.Pattern;

import org.json.JSONException;

public class Type
{
    private static Type singleton;

    private Type() {

    }

    public static synchronized Type getInstance()
    {
        if (singleton == null)
        {
            singleton = new Type();
        }

        return singleton;
    }

    public interface SQL
    {
        public static String _varchar = "varchar";

        public static String _text = "text";

        public static String _json = "json";

        public static String _int = "int";

        public static String _bigint = "bigint";

        public static String _bigserial = "bigserial";
        
        public static String _numeric = "numeric(15,2)";

        public static String _time = "time";

        public static String _double_precision = "double precision";

        public static String _real = "real";

        public static String _null = "null";

        public static String _boolean = "boolean";

        public static String _date = "date";

        public static String _timestamp = "timestamp";

        public interface Code
        {
            public static int VARCHAR = Types.VARCHAR;

            public static int TEXT = Types.CLOB;

            public static int INTEGER = Types.INTEGER;

            public static int BIGINT = Types.BIGINT;

            public static int NUMERIC = Types.NUMERIC;

            public static int TIME = Types.TIME;

            public static int DOUBLE = Types.DOUBLE;

            public static int FLOAT = Types.FLOAT;

            public static int BOOLEAN = Types.BOOLEAN;

            public static int DATE = Types.DATE;

            public static int TIMESTAMP = Types.TIMESTAMP;

            public static int NULL = Types.NULL;
        }
    }

    public interface YC
    {
        public static String String = "String";

        public static String Text = "Text";

        public static String Number = "Number";

        public static String Money = "Money";

        public static String Integer = "Integer";

        public static String Long = "Long";

        public static String Double = "Double";

        public static String Float = "Float";

        public static String Boolean = "Boolean";

        public static String Date = "Date";

        public static String Timestamp = "Timestamp";

        public static String Time = "Time";

        public static String Null = "Null";

        public static String UUID = "UUID";

        public static String TUUID = "TUUID";

        public static String Image = "Image";

        public static String PDF = "PDF";

        public static String Binary = "Binary";

        public static String File = "File";
    }

    public String getSQLTypeNameByYCType(String type, Integer length) throws JSONException
    {
        switch (type)
        {
            case YC.String:
            {
                return (length == null || length <= 0) ? SQL._varchar
                        : SQL._varchar.concat("(").concat(String.valueOf(length)).concat(")");
            }
            case YC.Date:
            {
                return SQL._date;
            }
            case YC.Boolean:
            {
                return SQL._boolean;
            }
            case YC.Time:
            {
                return SQL._time;
            }
            case YC.Timestamp:
            {
                return SQL._timestamp;
            }
            case YC.Long:
            {
                return SQL._bigint;
            }
            case YC.Integer:
            {
                return SQL._int;
            }
            case YC.Money:
            {
                return SQL._numeric;
            }
            case YC.Double:
            {
                return SQL._double_precision;
            }
            case YC.Float:
            {
                return SQL._real;
            }
            case YC.Text:
            {
                return SQL._text;
            }
            case YC.Image:
            {
                return SQL._text;
            }
            case YC.Null:
            {
                return SQL._null;
            }
            case YC.PDF:
            {
                return SQL._text;
            }
            case YC.Binary:
            {
                return SQL._text;
            }
            default:
            {
    
                return null;
            }
        }
    }

    public Integer getSQLTypeId(String ycType)
    {
        switch (ycType)
        {
            case YC.String:
            {
                return Types.VARCHAR;
            }
            case YC.Date:
            {
                return Types.DATE;
            }
            case YC.Boolean:
            {
                return Types.BOOLEAN;
            }
            case YC.Time:
            {
                return Types.TIME;
            }
            case YC.Timestamp:
            {
                return Types.TIMESTAMP;
            }
            case YC.Long:
            {
                return Types.BIGINT;
            }
            case YC.Integer:
            {
                return Types.INTEGER;
            }
            case YC.Money:
            {
                return Types.NUMERIC;
            }
            case YC.Double:
            {
                return Types.DOUBLE;
            }
            case YC.Float:
            {
                return Types.REAL;
            }
            case YC.Null:
            {
                return Types.NULL;
            }
            case YC.Text:
            {
                return Types.CLOB;
            }
            case YC.Image:
            {
                return Types.BLOB;
            }
            case YC.PDF:
            {
                return Types.BLOB;
            }
            case YC.Binary:
            {
                return Types.BLOB;
            }
            default:
            {
    
                return null;
            }
        }
    }

    public String getYCTypeBySQLTypeId(Integer id)
    {
        switch (id)
        {
            case Types.VARCHAR:
            {
                return YC.String;
            }
            case Types.INTEGER:
            {
                return YC.Integer;
            }
            case Types.BIGINT:
            {
                return YC.Long;
            }
            case Types.DATE:
            {
                return YC.Date;
            }
            case Types.NUMERIC:
            {
                return YC.Money;
            }
            case Types.BOOLEAN:
            {
                return YC.Boolean;
            }
            case -7:
            {
                return YC.Boolean;
            }
            case Types.NULL:
            {
                return YC.Null;
            }
            case Types.TIME:
            {
                return YC.Time;
            }
            case Types.TIMESTAMP:
            {
                return YC.Timestamp;
            }
            case Types.DOUBLE:
            {
                return YC.Double;
            }
            case Types.REAL:
            {
                return YC.Float;
            }
            case Types.BLOB:
            {
                return YC.Binary;
            }
            case Types.CLOB:
            {
                return YC.Text;
            }
            default:
            {
    
                return null;
            }
        }
    }

    public String getYCTypeBySQLTypeName(String typeName)
    {
        typeName = typeName.trim().toLowerCase();
        switch (typeName)
        {
            case SQL._varchar:
            {
                return YC.String;
            }
            case SQL._date:
            {
                return YC.Date;
            }
            case SQL._boolean:
            {
                return YC.Boolean;
            }
            case SQL._timestamp:
            {
                return YC.Timestamp;
            }
            case SQL._numeric:
            {
                return YC.Money;
            }
            case SQL._time:
            {
                return YC.Time;
            }
            case SQL._bigint:
            {
                return YC.Long;
            }
            case SQL._int:
            {
                return YC.Integer;
            }
            case SQL._double_precision:
            {
                return YC.Double;
            }
            case SQL._real:
            {
                return YC.Float;
            }
            case SQL._null:
            {
                return YC.Null;
            }
            case SQL._text:
            {
                return YC.Text;
            }
            default:
            {
    
                return null;
            }
        }
    }

    public Boolean isYCCanonicalType(String type)
    {
        switch (type)
        {
            case YC.String:
            {
                return true;
            }
            case YC.Long:
            {
                return true;
            }
            case YC.Integer:
            {
                return true;
            }
            case YC.Double:
            {
                return true;
            }
            case YC.Boolean:
            {
                return true;
            }
            case YC.Money:
            {
                return true;
            }
            case YC.Time:
            {
                return true;
            }
            case YC.Date:
            {
                return true;
            }
            case YC.Text:
            {
                return true;
            }
            case YC.Timestamp:
            {
                return true;
            }
            case YC.UUID:
            {
                return true;
            }
            case YC.TUUID:
            {
                return true;
            }
            case YC.Number:
            {
                return true;
            }
            case YC.Float:
            {
                return true;
            }
            default:
            {
                return false;
            }
        }
    }

    @SuppressWarnings("deprecation")
    synchronized public String getValueFormattedByType(Object value, String type)
    {
        if (value == null)
        {
            return null;
        }

        switch (type)
        {
            case YC.String:
            {
                return "'".concat(value.toString().trim()).concat("'");
            }
            case YC.Text:
            {
                return "'".concat(value.toString().trim()).concat("'");
            }
            case YC.Date:
            {
                return "'".concat(value.toString().trim()).concat("'");
            }
            case YC.Timestamp:
            {
                //String a = "2022-08-15 19:22:00.665";

                String [] auxiliar = value.toString().split(" ");

                String [] date = auxiliar[0].split(Pattern.quote("-"));
                Integer yyyy = Integer.parseInt(date[0]);
                Integer MM = Integer.parseInt(date[1]);
                Integer dd = Integer.parseInt(date[2]);

                String [] time = auxiliar[1].split(Pattern.quote(":"));
                Integer hh = Integer.parseInt(time[0]);
                Integer mm = Integer.parseInt(time[1]);

                if (time[2].indexOf(".") > 0) 
                {
                    String [] fraction = time[2].split(Pattern.quote("."));
                    Integer ss = Integer.parseInt(fraction[0]);
                    Integer nn = Integer.parseInt(fraction[1]);

                    return "'".concat(String.valueOf(new java.sql.Timestamp(yyyy-1900, MM-1, dd, hh, mm, ss, nn))).concat("'");
                }
                else
                {
                    //String [] fraction = time[2].split(Pattern.quote("."));
                    Integer ss = Integer.parseInt(time[2]);

                    return "'".concat(String.valueOf(new java.sql.Timestamp(yyyy-1900, MM-1, dd, hh, mm, ss, 000))).concat("'");
                }
            }
            case YC.Time:
            {
                return "'".concat(value.toString()).concat("'");
            }
            default:
            {
                return value.toString().trim();
            }
        }
    }
}