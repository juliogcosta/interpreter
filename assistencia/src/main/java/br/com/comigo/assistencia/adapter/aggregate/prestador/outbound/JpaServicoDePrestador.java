package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound;

import java.util.List;
import java.util.UUID;

import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "servico_de_prestador", schema = "assistencia")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JpaServicoDePrestador {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "prestador_id", nullable = false)
    private JpaPrestador prestador;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(schema = "assistencia", name = "item_de_servico_de_prestador", joinColumns = @JoinColumn(name = "servico_de_prestador_id"))
    @ToString.Exclude
    private List<JpaItemDeServicoDePrestador> itemDeServicoDePrestadors;

    public JpaServicoDePrestador(ServicoDePrestador servico) {
        this.id = servico.getId();
        this.status = servico.getStatus();
        if (servico.getItemDeServicoDePrestadors() == null) {
        	
        } else if (servico.getItemDeServicoDePrestadors().size() == 0) {
        	
        } else {
            this.setItemDeServicoDePrestadors(servico.getItemDeServicoDePrestadors().stream()
            		.map(item -> {
            			return new JpaItemDeServicoDePrestador(
            					item.getNome(),
            					item.getUnidadeDeMedida(), 
            					item.getPrecoUnitario());
            		}).toList());
        }
    }

	@Override
	public String toString() {
		return "JpaServicoDePrestador [id=" + id + ", status=" + status
				+ ", itemDeServicoDePrestadors=" + itemDeServicoDePrestadors + "]";
	}

}