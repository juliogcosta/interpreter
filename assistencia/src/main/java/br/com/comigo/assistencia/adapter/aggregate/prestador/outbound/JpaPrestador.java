package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound;

import java.util.ArrayList;
import java.util.List;

import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaCnpj;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEmail;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaEndereco;
import br.com.comigo.assistencia.adapter.aggregate.outbound.util.JpaTelefone;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador.Status;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prestador", schema = "assistencia")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JpaPrestador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String nome;

	@Embedded
	private JpaCnpj cnpj;

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

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;

	@Column(nullable = false)
	private Boolean temCertificacaoIso;

	@OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<JpaServicoDePrestador> servicoDePrestadors = new ArrayList<>();

	public JpaPrestador(Prestador prestador) {
		this.nome = prestador.getNome();
		this.cnpj = new JpaCnpj(prestador.getCnpj().cnpj());
		this.telefone = new JpaTelefone(prestador.getTelefone().numero(), prestador.getTelefone().tipo());
		this.whatsapp = new JpaTelefone(prestador.getWhatsapp().numero(), prestador.getWhatsapp().tipo());
		this.email = new JpaEmail(prestador.getEmail().email());
		this.endereco = new JpaEndereco(prestador.getEndereco().tipo(), prestador.getEndereco().logradouro(), prestador.getEndereco().numero(),
				prestador.getEndereco().complemento(), prestador.getEndereco().bairro(),
				prestador.getEndereco().cidade(), prestador.getEndereco().estado(), prestador.getEndereco().cep());
		this.status = Status.ATIVO;
	}

	@Override
	public String toString() {
		return "JpaPrestador [id=" + id + ", nome=" + nome + ", cnpj=" + cnpj + ", telefone=" + telefone + ", whatsapp="
				+ whatsapp + ", email=" + email + ", endereco=" + endereco + ", status=" + status
				+ ", temCertificacaoIso=" + temCertificacaoIso + ", servicoDePrestadors=" + servicoDePrestadors + "]";
	}

}