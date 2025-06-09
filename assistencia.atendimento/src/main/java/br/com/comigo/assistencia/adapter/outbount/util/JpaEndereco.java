package br.com.comigo.assistencia.adapter.outbount.util;

import br.com.comigo.common.model.records.Endereco.Tipo;
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
    @Column(name = "tipo", nullable = true)
    private Tipo tipo;

    @Column(name = "logradouro", nullable = false)
    private String logradouro;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "complemento", nullable = true)
    private String complemento;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "cep", nullable = false, length = 8)
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