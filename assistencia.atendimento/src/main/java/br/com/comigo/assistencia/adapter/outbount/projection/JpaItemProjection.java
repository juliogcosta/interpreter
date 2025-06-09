package br.com.comigo.assistencia.adapter.outbount.projection;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JpaItemProjection implements Serializable {

	private static final long serialVersionUID = -3683979645066851722L;

	private String nome;
	private String unidadeDeMedida;
	private Integer precoUnitario;
	private Integer quantidade;
	private String observacao;
	
	@Override
	public String toString() {
		return "JpaItemProjection [nome=" + nome + ", unidadeDeMedida=" + unidadeDeMedida + ", precoUnitario="
				+ precoUnitario + ", quantidade=" + quantidade + ", observacao=" + observacao + "]";
	}
	
}
