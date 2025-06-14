package com.yc.persistence.exception;

import java.io.Serializable;

public class InterpreterControlledException extends Exception implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 7190185206568560324L;

    public InterpreterControlledException()
    {
        
    }
    
    public InterpreterControlledException(String message)
    {
        super(message);
    }
}
