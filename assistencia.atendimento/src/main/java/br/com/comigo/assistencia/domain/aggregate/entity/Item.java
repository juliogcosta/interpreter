package br.com.comigo.assistencia.domain.aggregate.entity;

public class Item {

	private String nome;
	private String unidadeDeMedida;
	private Integer precoUnitario;
	private Integer quantidade;
	private String observacao;

	public Item() {
	}

	public Item(String nome, String unidadeDeMedida, Integer precoUnitario, Integer quantidade, String observacao) {
		super();
		this.nome = nome;
		this.unidadeDeMedida = unidadeDeMedida;
		this.precoUnitario = precoUnitario;
		this.quantidade = quantidade;
		this.observacao = observacao;
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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public String toString() {
		return "Item [nome=" + nome + ", unidadeDeMedida=" + unidadeDeMedida + ", precoUnitario=" + precoUnitario
				+ ", quantidade=" + quantidade + ", observacao=" + observacao + "]";
	}

}
