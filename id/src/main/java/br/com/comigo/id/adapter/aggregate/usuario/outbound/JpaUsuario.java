package br.com.comigo.id.adapter.aggregate.usuario.outbound;

import java.util.List;

import br.com.comigo.id.adapter.util.JpaEmail;
import br.com.comigo.id.adapter.util.JpaTelefone;
import br.com.comigo.id.domain.aggregate.usuario.Usuario;
import br.com.comigo.id.domain.util.StatusDeUsuario;
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
@Table(schema = "id", name = "usuario")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JpaUsuario {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", updatable = false, nullable = false)
        private Long id;

        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String nome;

        @Embedded
        @AttributeOverrides({
                        @AttributeOverride(name = "numero", column = @Column(name = "telefone_numero")),
                        @AttributeOverride(name = "tipo", column = @Column(name = "telefone_tipo"))
        })
        private JpaTelefone telefone;

        @Embedded
        private JpaEmail email;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StatusDeUsuario status;

        @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
        private List<JpaPapelDeUsuario> papelDeUsuarios;

        public JpaUsuario(Usuario usuario) {
                this.username = usuario.getUsername();
                this.nome = usuario.getNome();
                this.telefone = new JpaTelefone(usuario.getTelefone().numero(), usuario.getTelefone().tipo());
                this.email = new JpaEmail(usuario.getEmail().email());
                this.status = usuario.getStatus();
        }

        @Override
        public String toString() {
                return "{" +
                        " id='" + getId() + "'" +
                        ", username='" + getUsername() + "'" +
                        ", password='" + getPassword() + "'" +
                        ", nome='" + getNome() + "'" +
                        ", telefone='" + getTelefone() + "'" +
                        ", email='" + getEmail() + "'" +
                        ", status='" + getStatus() + "'" +
                        ", veiculos='" + getPapelDeUsuarios() + "'" +
                        "}";
        }

}