package com.yc.persistence.exception;

import java.io.Serializable;

public class YCException extends Exception implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9035449230007697716L;

	private String xTenantID = null;

	private String httpMethodType = null;

	private String httpUrl = null;

	private String username = null;
	
	private String code = null;
	
	private String local = null;

	public String getxTenantID()
	{
		return xTenantID;
	}

	public void setxTenantID(String tenantId)
	{
		this.xTenantID = tenantId;
	}

	public String getHttpMethodType()
	{
		return httpMethodType;
	}

	public void setHttpMethodType(String httpMethodType)
	{
		this.httpMethodType = httpMethodType;
	}

	public String getHttpUrl()
	{
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl)
	{
		this.httpUrl = httpUrl;
	}

	public String getLocal()
	{
		return local;
	}

	public void setLocal(String local)
	{
		this.local = local;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public YCException(String message) {
		super(message);
	}

	public YCException(Throwable cause) {
		super(cause);
	}

	public YCException(Throwable cause, String tenantId, String httpMethodType, String httpUrl, String username, String code, String local) {
		super(cause);
		this.xTenantID = tenantId;
		this.httpMethodType = httpMethodType;
		this.httpUrl = httpUrl;
		this.username = username;
		this.code = code;
		this.local = local;
	}

	public YCException(String message, Throwable cause) {
		super(message, cause);
	}

	public YCException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
