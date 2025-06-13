package com.yc.core.common.validator;

public class CNPJ {

    // Método para validar CNPJ
    public boolean validar(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se o CNPJ tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (caso inválido)
        if (todosDigitosIguais(cnpj)) {
            return false;
        }

        // Calcula e verifica o primeiro dígito verificador
        int digito1 = calcularDigito(cnpj.substring(0, 12), 5);
        if (digito1 != Character.getNumericValue(cnpj.charAt(12))) {
            return false;
        }

        // Calcula e verifica o segundo dígito verificador
        int digito2 = calcularDigito(cnpj.substring(0, 13), 6);
        return digito2 == Character.getNumericValue(cnpj.charAt(13));
    }

    // Método auxiliar para verificar se todos os dígitos são iguais
    private boolean todosDigitosIguais(String cnpj) {
        char primeiroDigito = cnpj.charAt(0);
        for (int i = 1; i < cnpj.length(); i++) {
            if (cnpj.charAt(i) != primeiroDigito) {
                return false;
            }
        }
        return true;
    }

    // Método para calcular dígito verificador
    private int calcularDigito(String str, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;
        
        for (int i = 0; i < str.length(); i++) {
            soma += Character.getNumericValue(str.charAt(i)) * peso;
            peso--;
            if (peso == 1) {
                peso = 9;
            }
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    // Método para formatar CNPJ (opcional)
    public String formatar(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", "");
        return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + 
               cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + 
               cnpj.substring(12, 14);
    }
}