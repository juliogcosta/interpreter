package br.com.comigo.common.model.records;

public record Endereco(Tipo tipo, String logradouro, String numero, String complemento, String bairro, String cidade,
        String estado, String cep) {

	public enum Tipo {
	  RESIDENCIAL,
	  COMERCIAL
	}

    public Endereco {
    	/*if (tipo == null) {
            throw new IllegalArgumentException("Tipo não pode ser nulo");
        }
        if (logradouro == null || logradouro.isBlank()) {
            throw new IllegalArgumentException("Rua não pode ser nula ou vazia");
        }
        if (logradouro == null || logradouro.isBlank()) {
            throw new IllegalArgumentException("Rua não pode ser nula ou vazia");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Número não pode ser nulo ou vazio");
        }
        if (bairro == null || bairro.isBlank()) {
            throw new IllegalArgumentException("Bairro não pode ser nulo ou vazio");
        }
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("Cidade não pode ser nula ou vazia");
        }
        if (estado == null || estado.isBlank()) {
            throw new IllegalArgumentException("Estado não pode ser nulo ou vazio");
        }
        if (!isValidCep(cep)) {
            throw new IllegalArgumentException("CEP inválido");
        }*/
    }

    @SuppressWarnings("unused")
	private static boolean isValidCep(String cep) {
        cep = cep.replaceAll("[^0-9]", "");
        
        // Verifica se o CEP é nulo ou não possui exatamente 8 dígitos numéricos
        if (cep == null || !cep.matches("\\d{8}")) {
            return false;
        }

        // Aqui você pode adicionar uma validação adicional, como verificar se o CEP
        // existe
        // em uma base de dados ou API externa. Exemplo:
        // return ExternalCepValidator.isValid(cep);

        return true; // Retorna true se o formato básico for válido
    }
}