package br.com.comigo.id.adapter.aggregate.usuario.outbound;

import br.com.comigo.id.domain.aggregate.usuario.PapelDeUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "id", name = "papel_de_usuario")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JpaPapelDeUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private Long papelId;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private JpaUsuario usuario;

    public JpaPapelDeUsuario(PapelDeUsuario papelDeUsuario) {
        this.id = papelDeUsuario.getId();
        this.papelId = papelDeUsuario.getPapelId();
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", papelId='" + getPapelId() + "'" +
                "}";
    }
}