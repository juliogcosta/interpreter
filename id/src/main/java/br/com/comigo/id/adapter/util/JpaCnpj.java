package br.com.comigo.id.adapter.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class JpaCnpj {

    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;

    public JpaCnpj(String cnpj) {
        this.setCnpj(cnpj);
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw new IllegalArgumentException("CNPJ não pode ser nulo ou vazio");
        }
        if (!cnpj.matches("\\d{14}")) {
            throw new IllegalArgumentException("CNPJ deve conter 14 dígitos numéricos");
        }
        if (!isValidCnpj(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
        this.cnpj = cnpj;
    }

    private static boolean isValidCnpj(String cnpj) {
        if (cnpj.chars().distinct().count() == 1) {
            return false;
        }

        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum1 = 0;
        for (int i = 0; i < 12; i++) {
            sum1 += (cnpj.charAt(i) - '0') * weights1[i];
        }
        int checkDigit1 = 11 - (sum1 % 11);
        if (checkDigit1 >= 10) {
            checkDigit1 = 0;
        }

        if (checkDigit1 != (cnpj.charAt(12) - '0')) {
            return false;
        }

        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum2 = 0;
        for (int i = 0; i < 13; i++) {
            sum2 += (cnpj.charAt(i) - '0') * weights2[i];
        }
        int checkDigit2 = 11 - (sum2 % 11);
        if (checkDigit2 >= 10) {
            checkDigit2 = 0;
        }

        return checkDigit2 == (cnpj.charAt(13) - '0');
    }
}