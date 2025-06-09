package br.com.comigo.common.validator;

public class CPF {

    // Método principal para validar CPF
    public boolean validar(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (caso inválido)
        if (todosDigitosIguais(cpf)) {
            return false;
        }

        // Calcula e verifica o primeiro dígito verificador
        int digito1 = calcularDigito(cpf.substring(0, 9), 10);
        if (digito1 != Character.getNumericValue(cpf.charAt(9))) {
            return false;
        }

        // Calcula e verifica o segundo dígito verificador
        int digito2 = calcularDigito(cpf.substring(0, 10), 11);
        return digito2 == Character.getNumericValue(cpf.charAt(10));
    }

    // Método auxiliar para verificar se todos os dígitos são iguais
    private boolean todosDigitosIguais(String cpf) {
        char primeiroDigito = cpf.charAt(0);
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != primeiroDigito) {
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
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    // Método para formatar CPF (opcional)
    public String formatar(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }
}