package br.com.comigo.common.infrastructure.exception;

public class RegisterNotFoundException extends Exception {
    private static final long serialVersionUID = -121225676158572293L;

	public RegisterNotFoundException(String message) {
        super(message);
    }
}