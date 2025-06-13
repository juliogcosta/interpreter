package com.yc.core.common.model.records;

public record Cpf(String cpf) {

    public Cpf {
        /*if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio");
        }
        if (!cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos numéricos");
        }
        if (!isValidCpf(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }*/
    }

    @SuppressWarnings("unused")
	private static boolean isValidCpf(String valor) {
        // Verifica se todos os dígitos são iguais (ex.: 11111111111)
        if (valor.chars().distinct().count() == 1) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int sum1 = 0;
        for (int i = 0; i < 9; i++) {
            sum1 += (valor.charAt(i) - '0') * (10 - i);
        }
        int checkDigit1 = 11 - (sum1 % 11);
        if (checkDigit1 >= 10) {
            checkDigit1 = 0;
        }

        // Verifica o primeiro dígito
        if (checkDigit1 != (valor.charAt(9) - '0')) {
            return false;
        }

        // Calcula o segundo dígito verificador
        int sum2 = 0;
        for (int i = 0; i < 10; i++) {
            sum2 += (valor.charAt(i) - '0') * (11 - i);
        }
        int checkDigit2 = 11 - (sum2 % 11);
        if (checkDigit2 >= 10) {
            checkDigit2 = 0;
        }

        // Verifica o segundo dígito
        return checkDigit2 == (valor.charAt(10) - '0');
    }
}
