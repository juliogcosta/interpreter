package br.com.comigo.assistencia.adapter.outbount.projection;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.comigo.assistencia.domain.aggregate.entity.Servico.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "RM_SERVICO", schema = "ASSISTENCIA_ES")
@RequiredArgsConstructor
@Getter
@Setter
public class JpaServicoProjection implements Persistable<UUID>, Serializable {

    private static final long serialVersionUID = -4781077144881370785L;
    
	@Id
    private UUID id;
	
    private int version;
    
    private String nome;
    
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(nullable = false)
    private Boolean certificacaoIso;
    
    private Timestamp dataHoraDisponibilizado;
    
    private Timestamp dataHoraAjustado;
    
    private Timestamp dataHoraIndisponibilizado;
    
    private Timestamp dataHoraCancelado;

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
        JpaServicoProjection other = (JpaServicoProjection) o;
        return Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return 1;
    }

	@Override
	public String toString() {
		return "JpaServicoProjection [id=" + id + ", version=" + version + ", nome=" + nome
				+ ", descricao=" + descricao + ", status=" + status + ", certificacaoIso=" + certificacaoIso + ", dataHoraDisponibilizado="
				+ dataHoraDisponibilizado + ", dataHoraIndisponibilizado=" + dataHoraIndisponibilizado
				+ ", dataHoraAjustado=" + dataHoraAjustado + ", dataHoraCancelado=" + dataHoraCancelado + "]";
	}
}
