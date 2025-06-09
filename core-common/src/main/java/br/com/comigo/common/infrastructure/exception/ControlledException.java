package br.com.comigo.common.infrastructure.exception;

public class ControlledException extends Exception {
    private static final long serialVersionUID = -121000676158572293L;

	public ControlledException(String message) {
        super(message);
    }
}