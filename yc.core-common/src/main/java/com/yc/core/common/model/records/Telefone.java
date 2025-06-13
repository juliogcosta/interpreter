package com.yc.core.common.model.records;

public record Telefone(String numero, Tipo tipo) {

    //private static final Pattern TELEFONE_PATTERN = Pattern.compile("^\\(\\d{2}\\)[\\s-]?9\\d{4}[-\\s]?\\d{4}$");

	public enum Tipo {
	  PARTICULAR,
	  COMERCIAL
	}
	
    public Telefone {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Numero de Telefone não pode ser nulo ou vazio");
        }
        /*if (TELEFONE_PATTERN.matcher(numero).matches()) {

        } else {
        	throw new IllegalArgumentException("Telefone inválido");
        }*/
    }
}