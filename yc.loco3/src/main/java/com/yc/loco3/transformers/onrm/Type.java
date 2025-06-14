package com.yc.loco3.transformers.onrm;

import java.util.regex.Pattern;

import org.json.JSONException;

import com.datastax.driver.core.DataType;

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

    public interface CQL
    {
        public static String _varchar = DataType.varchar().getName().name();

        public static String _text = DataType.text().getName().name();

        public static String _int = DataType.cint().getName().name();

        public static String _bigint = DataType.bigint().getName().name();

        public static String _UUID = DataType.uuid().getName().name();

        public static String _TIMEUUID = DataType.timeuuid().getName().name();

        public static String _time = DataType.time().getName().name();

        public static String _double = DataType.cdouble().getName().name();

        public static String _float = DataType.cfloat().getName().name();

        public static String _boolean = DataType.cboolean().getName().name();

        public static String _date = DataType.date().getName().name();

        public static String _timestamp = DataType.timestamp().getName().name();

        public interface Code
        {
            public static int VARCHAR = DataType.varchar().getName().ordinal();

            public static int TEXT = DataType.text().getName().ordinal();

            public static int INTEGER = DataType.cint().getName().ordinal();

            public static int BIGINT = DataType.bigint().getName().ordinal();

            public static int UUID = DataType.uuid().getName().ordinal();

            public static int TIMEUUID = DataType.timeuuid().getName().ordinal();

            public static int TIME = DataType.time().getName().ordinal();

            public static int DOUBLE = DataType.cdouble().getName().ordinal();

            public static int FLOAT = DataType.cfloat().getName().ordinal();

            public static int BOOLEAN = DataType.cboolean().getName().ordinal();

            public static int DATE = DataType.date().getName().ordinal();

            public static int TIMESTAMP = DataType.timestamp().getName().ordinal();
        }
    }

    public interface YC
    {
        public static String String = "String";

        public static String Text = "Text";

        public static String Number = "Number";

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
    }

    public String getCQLTypeNameByYCType(String type, Integer length) throws JSONException
    {
        switch (type)
        {
            case YC.String:
            {
                return (length == null || length <= 0) ? CQL._varchar
                        : CQL._varchar.concat("(").concat(String.valueOf(length)).concat(")");
            }
            case YC.Date:
            {
                return CQL._date;
            }
            case YC.Boolean:
            {
                return CQL._boolean;
            }
            case YC.Time:
            {
                return CQL._time;
            }
            case YC.UUID:
            {
                return CQL._UUID;
            }
            case YC.TUUID:
            {
                return CQL._TIMEUUID;
            }
            case YC.Timestamp:
            {
                return CQL._timestamp;
            }
            case YC.Number:
            {
                return CQL._double;
            }
            case YC.Long:
            {
                return CQL._bigint;
            }
            case YC.Integer:
            {
                return CQL._int;
            }
            case YC.Double:
            {
                return CQL._double;
            }
            case YC.Float:
            {
                return CQL._float;
            }
            default:
            {
    
                return null;
            }
        }
    }

    public Integer getCQLTypeId(String ycType)
    {
        switch (ycType)
        {
            case YC.String:
            {
                return CQL.Code.TEXT;
            }
            case YC.UUID:
            {
                return CQL.Code.UUID;
            }
            case YC.TUUID:
            {
                return CQL.Code.TIMEUUID;
            }
            case YC.Date:
            {
                return CQL.Code.DATE;
            }
            case YC.Timestamp:
            {
                return CQL.Code.TIMESTAMP;
            }
            case YC.Boolean:
            {
                return CQL.Code.BOOLEAN;
            }
            case YC.Number:
            {
                return CQL.Code.DOUBLE;
            }
            case YC.Long:
            {
                return CQL.Code.BIGINT;
            }
            case YC.Integer:
            {
                return CQL.Code.INTEGER;
            }
            case YC.Double:
            {
                return CQL.Code.DOUBLE;
            }
            case YC.Float:
            {
                return CQL.Code.FLOAT;
            }
            default:
            {
    
                return null;
            }
        }
    }

    public String getYCTypeByCQLTypeId(Integer id)
    {
        if (CQL.Code.TEXT == id)
        {
            return YC.String;
        }
        else if (CQL.Code.VARCHAR == id)
        {
            return YC.String;
        }
        else if (CQL.Code.DATE == id)
        {
            return YC.Date;
        }
        else if (CQL.Code.BOOLEAN == id)
        {
            return YC.Boolean;
        }
        else if (CQL.Code.TIMESTAMP == id)
        {
            return YC.Timestamp;
        }
        else if (CQL.Code.BIGINT == id)
        {
            return YC.Long;
        }
        else if (CQL.Code.DATE == id)
        {
            return YC.Date;
        }
        else if (CQL.Code.INTEGER == id)
        {
            return YC.Integer;
        }
        else if (CQL.Code.DOUBLE == id)
        {
            return YC.Double;
        }
        else if (CQL.Code.DATE == id)
        {
            return YC.Date;
        }
        else return null;
    }

    public String getYCTypeByCQLTypeName(String typeName)
    {
        if (CQL._text.equals(typeName))
        {
            return YC.Text;
        }
        else if (CQL._varchar.equals(typeName))
        {
            return YC.String;
        }
        else if (CQL._UUID.equals(typeName))
        {
            return YC.UUID;
        }
        else if (CQL._TIMEUUID.equals(typeName))
        {
            return YC.TUUID;
        }
        else if (CQL._date.equals(typeName))
        {
            return YC.Date;
        }
        else if (CQL._time.equals(typeName))
        {
            return YC.Time;
        }
        else if (CQL._timestamp.equals(typeName))
        {
            return YC.Timestamp;
        }
        else if (CQL._boolean.equals(typeName))
        {
            return YC.Boolean;
        }
        else if (CQL._bigint.equals(typeName))
        {
            return YC.Long;
        }
        else if (CQL._int.equals(typeName))
        {
            return YC.Integer;
        }
        else if (CQL._float.equals(typeName))
        {
            return YC.Float;
        }
        else if (CQL._double.equals(typeName))
        {
            return YC.Double;
        }
        else return null;
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

                String [] fraction = time[2].split(Pattern.quote("."));
                Integer ss = Integer.parseInt(fraction[0]);
                Integer nn = Integer.parseInt(fraction[1]);

                return String.valueOf(new java.sql.Timestamp(yyyy-1900, MM-1, dd, hh, mm, ss, nn));
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