package br.com.comigo.assistencia.adapter.aggregate.cliente.outbound;

import java.util.Date;
import java.util.List;

import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaCpf;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEmail;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEndereco;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaTelefone;
import br.com.comigo.assistencia.domain.aggregate.cliente.Cliente;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cliente", schema = "assistencia")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JpaCliente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Embedded
	private JpaCpf cpf;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "numero", column = @Column(name = "telefone_numero")),
			@AttributeOverride(name = "tipo", column = @Column(name = "telefone_tipo")) })
	private JpaTelefone telefone;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "numero", column = @Column(name = "whatsapp_numero")),
			@AttributeOverride(name = "tipo", column = @Column(name = "whatsapp_tipo")) })
	private JpaTelefone whatsapp;

	@Embedded
	private JpaEmail email;

	@Embedded
	private JpaEndereco endereco;

	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "assistencia", name = "veiculo_de_cliente", joinColumns = @JoinColumn(name = "cliente_id"))
    @ToString.Exclude
	private List<JpaVeiculo> veiculos;

	public JpaCliente(Cliente cliente) {
		this.nome = cliente.getNome();
		this.cpf = new JpaCpf(cliente.getCpf().cpf());
		this.telefone = new JpaTelefone(cliente.getTelefone().numero(), cliente.getTelefone().tipo());
		this.whatsapp = new JpaTelefone(cliente.getWhatsapp().numero(), cliente.getWhatsapp().tipo());
		this.email = new JpaEmail(cliente.getEmail().email());
		this.endereco = new JpaEndereco(cliente.getEndereco().tipo(), cliente.getEndereco().logradouro(), 
				cliente.getEndereco().numero(), cliente.getEndereco().complemento(), cliente.getEndereco().bairro(), 
				cliente.getEndereco().cidade(), cliente.getEndereco().estado(), cliente.getEndereco().cep());
		this.dataNascimento = cliente.getDataNascimento();
		
        if (cliente.getVeiculos() == null) {
        	
        } else if (cliente.getVeiculos().size() == 0) {
        	
        } else {
            this.setVeiculos(cliente.getVeiculos().stream()
            		.map(veiculo -> {
            			return new JpaVeiculo(
            					veiculo.getMarca(),
            					veiculo.getModelo(), 
            					veiculo.getPlaca(), 
            					veiculo.getValidado());
            		}).toList());
        }
	}

	@Override
	public String toString() {
		return "JpaCliente [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", telefone=" + telefone + ", whatsapp="
				+ whatsapp + ", email=" + email + ", endereco=" + endereco + ", dataNascimento=" + dataNascimento
				+ ", veiculos=" + veiculos + "]";
	}

}