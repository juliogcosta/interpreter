package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JpaItemDeServicoDePrestador implements Serializable {

    private static final long serialVersionUID = -3755151538485509909L;
    
    private String nome = null;
	private String unidadeDeMedida = null;
	private Integer precoUnitario = null;
}
