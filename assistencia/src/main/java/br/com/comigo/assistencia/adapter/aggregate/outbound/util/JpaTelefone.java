package br.com.comigo.assistencia.adapter.aggregate.outbound.util;

import com.yc.core.common.model.records.Telefone.Tipo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class JpaTelefone {

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 15)
    private Tipo tipo;

    @Column(nullable = true, length = 15)
    private String numero;

    public JpaTelefone(String numero, Tipo tipo) {
        this.numero = numero;
        this.tipo = tipo;
    }
}