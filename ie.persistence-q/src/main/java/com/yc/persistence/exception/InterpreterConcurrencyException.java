package com.yc.persistence.exception;

import java.io.Serializable;

public class InterpreterConcurrencyException extends Exception implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public InterpreterConcurrencyException()
    {
        
    }
    
    public InterpreterConcurrencyException(String message)
    {
        super(message);
    }
}
