package br.com.comigo.common.model.records;

public record Cnpj(String cnpj) {

    public Cnpj {
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ não pode ser nulo ou vazio");
        }
        if (!validar(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }

    public static boolean validar(String cnpj) {
        /*
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se tem 14 dígitos e não é uma sequência de números iguais
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Calcula o primeiro dígito verificador
        int digito1 = calcularDigito(cnpj.substring(0, 12));

        // Calcula o segundo dígito verificador
        int digito2 = calcularDigito(cnpj.substring(0, 13));

        // Verifica se os dígitos calculados conferem com os informados
        return cnpj.substring(12).equals(String.format("%d%d", digito1, digito2));
         */
    	return true;
    }

    @SuppressWarnings("unused")
	private static int calcularDigito(String str) {
        int peso = 2;
        int soma = 0;

        // Calcula a soma ponderada dos dígitos
        for (int i = str.length() - 1; i >= 0; i--) {
            soma += Character.getNumericValue(str.charAt(i)) * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }

        // Calcula o dígito verificador
        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }
}