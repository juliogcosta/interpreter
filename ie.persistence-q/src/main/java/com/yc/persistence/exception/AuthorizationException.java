package com.yc.persistence.exception;

@SuppressWarnings("serial")
public class AuthorizationException extends Exception
{
	public AuthorizationException(String message)
	{
		super(message);
	}
}
