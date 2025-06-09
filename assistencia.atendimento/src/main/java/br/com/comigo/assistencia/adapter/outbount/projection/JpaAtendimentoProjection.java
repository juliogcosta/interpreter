package br.com.comigo.assistencia.adapter.outbount.projection;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.comigo.assistencia.adapter.outbount.util.JpaEndereco;
import br.com.comigo.assistencia.adapter.outbount.util.JpaTelefone;
import br.com.comigo.assistencia.domain.aggregate.entity.Atendimento.Status;
import br.com.comigo.common.model.records.TipoDeDocFiscal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "RM_ATENDIMENTO", schema = "ASSISTENCIA_ES")
@RequiredArgsConstructor
@Getter
@Setter
public class JpaAtendimentoProjection implements Persistable<UUID>, Serializable {

	private static final long serialVersionUID = -4781077144881370785L;

	@Id
	@Column(nullable = false)
	private UUID id;

	private int version;

	@Enumerated(EnumType.STRING)
	private Status status;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(schema = "ASSISTENCIA_ES", name = "RM_ATENDIMENTO_ITEM", joinColumns = @JoinColumn(name = "ATENDIMENTO_ID"))
	@ToString.Exclude
	private List<JpaItemProjection> items = new ArrayList<>();

	private String prestadorId;

	private String clienteId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoDeDocFiscal clienteTipoDeDocFiscal;

	@Column(nullable = false)
    private String clienteDocFiscal;

	@Column(nullable = false)
	private String clienteNome;

	@Column(nullable = false)
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "numero", column = @Column(name = "cliente_telefone_numero")),
			@AttributeOverride(name = "tipo", column = @Column(name = "cliente_telefone_tipo")) })
	private JpaTelefone clienteTelefone;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "numero", column = @Column(name = "cliente_whatsapp_numero")),
			@AttributeOverride(name = "tipo", column = @Column(name = "cliente_whatsapp_tipo")) })
	private JpaTelefone clienteWhatsapp;

	@Column(nullable = false)
	private String veiculoPlaca;

	@Column(nullable = false)
	private String tipoOcorrencia;

	@Column(nullable = false)
	private String servicoId;

	@Column(nullable = false)
	private String servicoNome;

	@Column(nullable = false)
	private Timestamp dataHoraSolicitado;

	private Timestamp dataHoraConfirmado;

	private Timestamp dataHoraAjustado;

	private Timestamp dataHoraFinalizado;

	private Timestamp dataHoraCancelado;

	private String descricao;

	@Column(nullable = false)
	@Embedded
	@AttributeOverrides({ 
			@AttributeOverride(name = "logradouro", column = @Column(name = "base_endereco_logradouro")),
			@AttributeOverride(name = "numero", column = @Column(name = "base_endereco_numero")),
			@AttributeOverride(name = "complemento", column = @Column(name = "base_endereco_complemento")),
			@AttributeOverride(name = "bairro", column = @Column(name = "base_endereco_bairro")),
			@AttributeOverride(name = "cidade", column = @Column(name = "base_endereco_cidade")),
			@AttributeOverride(name = "estado", column = @Column(name = "base_endereco_estado")),
			@AttributeOverride(name = "cep", column = @Column(name = "base_endereco_cep")),
			@AttributeOverride(name = "tipo", column = @Column(name = "base_endereco_tipo", insertable = false, updatable = false)) })
	private JpaEndereco base;

	@Column(nullable = false)
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "logradouro", column = @Column(name = "origem_endereco_logradouro")),
			@AttributeOverride(name = "numero", column = @Column(name = "origem_endereco_numero")),
			@AttributeOverride(name = "complemento", column = @Column(name = "origem_endereco_complemento")),
			@AttributeOverride(name = "bairro", column = @Column(name = "origem_endereco_bairro")),
			@AttributeOverride(name = "cidade", column = @Column(name = "origem_endereco_cidade")),
			@AttributeOverride(name = "estado", column = @Column(name = "origem_endereco_estado")),
			@AttributeOverride(name = "cep", column = @Column(name = "origem_endereco_cep")),
			@AttributeOverride(name = "tipo", column = @Column(name = "base_endereco_tipo", insertable = false, updatable = false)) })
	private JpaEndereco origem;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "logradouro", column = @Column(name = "destino_endereco_logradouro")),
			@AttributeOverride(name = "numero", column = @Column(name = "destino_endereco_numero")),
			@AttributeOverride(name = "complemento", column = @Column(name = "destino_endereco_complemento")),
			@AttributeOverride(name = "bairro", column = @Column(name = "destino_endereco_bairro")),
			@AttributeOverride(name = "cidade", column = @Column(name = "destino_endereco_cidade")),
			@AttributeOverride(name = "estado", column = @Column(name = "destino_endereco_estado")),
			@AttributeOverride(name = "cep", column = @Column(name = "destino_endereco_cep")),
			@AttributeOverride(name = "tipo", column = @Column(name = "base_endereco_tipo", insertable = false, updatable = false)) })
	private JpaEndereco destino;

	@JsonIgnore
	@Override
	public boolean isNew() {
		return version <= 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (getClass() != o.getClass()) {
			return false;
		}
		JpaAtendimentoProjection other = (JpaAtendimentoProjection) o;
		return Objects.equals(id, other.getId());
	}

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public String toString() {
		return "JpaAtendimentoProjection [id=" + id + ", version=" + version + ", status=" + status + ", items=" + items
				+ ", prestadorId=" + prestadorId + ", clienteId=" + clienteId + ", clienteTipoDeDocFiscal="
				+ clienteTipoDeDocFiscal + ", clienteDocFiscal=" + clienteDocFiscal + ", clienteNome=" + clienteNome
				+ ", clienteTelefone=" + clienteTelefone + ", clienteWhatsapp=" + clienteWhatsapp + ", veiculoPlaca="
				+ veiculoPlaca + ", tipoOcorrencia=" + tipoOcorrencia + ", servicoId=" + servicoId + ", servicoNome="
				+ servicoNome + ", dataHoraSolicitado=" + dataHoraSolicitado + ", dataHoraConfirmado="
				+ dataHoraConfirmado + ", dataHoraAjustado=" + dataHoraAjustado + ", dataHoraFinalizado="
				+ dataHoraFinalizado + ", dataHoraCancelado=" + dataHoraCancelado + ", descricao=" + descricao
				+ ", base=" + base + ", origem=" + origem + ", destino=" + destino + "]";
	}

}
