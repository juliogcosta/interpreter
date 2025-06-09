package br.com.comigo.common.infrastructure.exception;

public class BusinessRuleConsistencyException extends Exception {
    private static final long serialVersionUID = 6192574254545406503L;

	public BusinessRuleConsistencyException(String message) {
        super(message);
    }
}