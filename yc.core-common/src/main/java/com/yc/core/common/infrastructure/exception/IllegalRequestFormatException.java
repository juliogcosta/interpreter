package com.yc.core.common.infrastructure.exception;

public class IllegalRequestFormatException extends Exception {

	private static final long serialVersionUID = -5591815471979515450L;

	public IllegalRequestFormatException(Exception e) {
		super(e);
	}

	public IllegalRequestFormatException(String message) {
		super(message);
	}

	public IllegalRequestFormatException(String message, Exception e) {
		super(message, e);
	}
}
