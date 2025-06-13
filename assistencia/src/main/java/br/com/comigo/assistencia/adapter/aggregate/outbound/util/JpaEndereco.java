package br.com.comigo.assistencia.adapter.aggregate.outbound.util;

import com.yc.core.common.model.records.Endereco.Tipo;

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
public class JpaEndereco {

	@Enumerated(EnumType.STRING)
    @Column(name = "endereco_tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "endereco_logradouro", nullable = false)
    private String logradouro;

    @Column(name = "endereco_numero", nullable = false)
    private String numero;

    @Column(name = "endereco_complemento", nullable = true)
    private String complemento;

    @Column(name = "endereco_bairro", nullable = false)
    private String bairro;

    @Column(name = "endereco_cidade", nullable = false)
    private String cidade;

    @Column(name = "endereco_estado", nullable = false)
    private String estado;

    @Column(name = "endereco_cep", nullable = false, length = 8)
    private String cep;

    public JpaEndereco(Tipo tipo, String logradouro, String numero, String complemento, String bairro, String cidade,
            String estado, String cep) {
    	this.tipo = tipo;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
}