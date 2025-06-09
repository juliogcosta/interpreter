package br.com.comigo.id.adapter.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class JpaCpf {

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    public JpaCpf(String cpf) {
        this.setCpf(cpf);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}