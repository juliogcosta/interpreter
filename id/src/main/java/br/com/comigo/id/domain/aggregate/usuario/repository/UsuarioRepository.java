package br.com.comigo.id.domain.aggregate.usuario.repository;

import java.util.List;
import java.util.Optional;

import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.id.domain.aggregate.usuario.Usuario;
import br.com.comigo.id.domain.projection.UsuarioAndPapelProjection;
import br.com.comigo.id.domain.util.StatusDePapel;
import br.com.comigo.id.domain.util.StatusDeUsuario;

public interface UsuarioRepository {
    public Usuario create(Usuario usuario);

    public void update(Usuario usuario);

    public Optional<Usuario> findById(Long id);

    public Optional<Usuario> findByUsername(String username);

    public List<Usuario> findByNome(String nome);

    public List<Usuario> findByTelefone(Telefone telefone);

    public void deleteById(Long id);

    public List<UsuarioAndPapelProjection> findUsuarioVsPapel(String username, StatusDeUsuario uStatus, StatusDePapel pStatus);
}