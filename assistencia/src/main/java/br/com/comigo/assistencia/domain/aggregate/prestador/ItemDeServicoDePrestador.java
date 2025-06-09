package br.com.comigo.assistencia.domain.aggregate.prestador;

public class ItemDeServicoDePrestador {

    private String nome = null;
    private String unidadeDeMedida = null;
	private Integer precoUnitario = null;
	
    public ItemDeServicoDePrestador() {
	}
    
    public ItemDeServicoDePrestador(String nome, String unidadeDeMedida, Integer precoUnitario) {
    	this.nome = nome;
    	this.unidadeDeMedida = unidadeDeMedida;
    	this.precoUnitario = precoUnitario;
    }
    
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUnidadeDeMedida() {
		return unidadeDeMedida;
	}

	public void setUnidadeDeMedida(String unidadeDeMedida) {
		this.unidadeDeMedida = unidadeDeMedida;
	}
	
	public Integer getPrecoUnitario() {
		return precoUnitario;
	}
    
	public void setPrecoUnitario(Integer precoUnitario) {
		this.precoUnitario = precoUnitario;
	}
}
