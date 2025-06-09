package br.com.comigo.id.adapter.aggregate.usuario.outbound.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.comigo.id.adapter.aggregate.usuario.outbound.JpaUsuario;
import br.com.comigo.id.domain.projection.UsuarioAndPapelProjection;
import br.com.comigo.id.domain.util.StatusDePapel;
import br.com.comigo.id.domain.util.StatusDeUsuario;

public interface JpaUsuarioRepository extends JpaRepository<JpaUsuario, Long> {
    public Optional<JpaUsuario> findByUsername(String username);

    public List<JpaUsuario> findByNome(String nome);

    public List<JpaUsuario> findByTelefone_Numero(String numero);

    @Query("SELECT " + 
        "u.id AS id, " + 
        "u.username AS username, " + 
        "u.password AS password, " + 
        "u.nome AS nome, " + 
        "u.email AS email, " + 
        "u.telefone AS telefone, " + 
        "u.status AS status, " + 
        "p.status AS papelStatus, " +
        "p.nome AS papelNome " + 
        "FROM JpaUsuario u " + 
            "LEFT JOIN JpaPapelDeUsuario pu ON u.id = pu.usuario.id " +
            "LEFT JOIN JpaPapel p ON p.id = pu.papelId " +
        "WHERE u.username = :username AND u.status = :uStatus AND p.status = :pStatus")
    public List<UsuarioAndPapelProjection> findUsuarioVsPapel(String username, StatusDeUsuario uStatus, StatusDePapel pStatus);

}