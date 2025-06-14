package com.yc.models.sql.up;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PrimitiveType
{
    static public enum Type {
        String, Text, Integer, Long, Double, Date, Time, Boolean
    };

    private String id;

    @NotBlank
    @NotEmpty
    @NotNull
    private String name;

    private String comment;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name.toString();
    }

    public void setName(@NotNull @NotBlank String name) throws Exception
    {
        if (name.equals(Type.String.toString()))
        {
            name = Type.String.name();
        }
        else if (name.equals(Type.Text.toString()))
        {
            name = Type.Text.name();
        }
        else if (name.equals(Type.Integer.toString()))
        {
            name = Type.Integer.name();
        }
        else if (name.equals(Type.Long.toString()))
        {
            name = Type.Long.name();
        }
        else if (name.equals(Type.Double.toString()))
        {
            name = Type.Double.name();
        }
        else if (name.equals(Type.Date.toString()))
        {
            name = Type.Date.name();
        }
        else if (name.equals(Type.Time.toString()))
        {
            name = Type.Time.name();
        }
        else if (name.equals(Type.Boolean.toString()))
        {
            name = Type.Boolean.name();
        }
        else
        {
            throw new Exception("type unknow!");
        }
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }
}
