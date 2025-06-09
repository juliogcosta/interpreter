package br.com.comigo.common.infrastructure.exception;

public class IncompleteRegisterException extends Exception {
    private static final long serialVersionUID = 6192574254545416503L;

	public IncompleteRegisterException(String message) {
        super(message);
    }
}