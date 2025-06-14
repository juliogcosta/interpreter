package com.yc.persistence.exception;

import java.io.Serializable;

public class InterpreterDuplicatedRecordException extends Exception implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 7190185206568560324L;

    public InterpreterDuplicatedRecordException()
	{
		
	}
    public InterpreterDuplicatedRecordException(String message)
    {
    	super(message);
	}
}
