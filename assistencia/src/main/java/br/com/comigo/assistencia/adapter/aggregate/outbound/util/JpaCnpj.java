package br.com.comigo.assistencia.adapter.aggregate.outbound.util;

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
        this.cnpj = cnpj;
    }
}