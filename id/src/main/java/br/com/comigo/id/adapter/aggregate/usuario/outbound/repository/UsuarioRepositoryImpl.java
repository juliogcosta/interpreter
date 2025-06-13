package br.com.comigo.id.adapter.aggregate.usuario.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.yc.core.common.model.records.Telefone;

import br.com.comigo.id.adapter.aggregate.usuario.outbound.JpaUsuario;
import br.com.comigo.id.adapter.util.JpaEmail;
import br.com.comigo.id.adapter.util.JpaTelefone;
import br.com.comigo.id.domain.aggregate.usuario.Usuario;
import br.com.comigo.id.domain.aggregate.usuario.repository.UsuarioRepository;
import br.com.comigo.id.domain.projection.UsuarioAndPapelProjection;
import br.com.comigo.id.domain.util.StatusDePapel;
import br.com.comigo.id.domain.util.StatusDeUsuario;
import br.com.comigo.id.mapper.aggregate.usuario.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario create(Usuario usuario) {
        JpaUsuario jpaUsuario = new JpaUsuario(usuario);
        jpaUsuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        jpaUsuario = this.jpaUsuarioRepository.save(jpaUsuario);
        usuario.setId(jpaUsuario.getId());
        return usuario;
    }

    @Override
    public void update(Usuario usuario) {
        JpaUsuario jpaUsuario = this.jpaUsuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));
        jpaUsuario.setPassword(usuario.getPassword());
        jpaUsuario.setNome(usuario.getNome());
        jpaUsuario.setEmail(new JpaEmail(usuario.getEmail().email()));
        jpaUsuario.setTelefone(new JpaTelefone(usuario.getTelefone().numero(), usuario.getTelefone().tipo()));
        jpaUsuario.setStatus(usuario.getStatus());
        this.jpaUsuarioRepository.save(jpaUsuario);
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        Optional<JpaUsuario> optional = this.jpaUsuarioRepository.findById(id);
        return optional.map(usuarioMapper::fromJpaToDomain);
    }

    @Override
    public Optional<Usuario> findByUsername(String username) {
        Optional<JpaUsuario> optional = this.jpaUsuarioRepository.findByUsername(username);
        return optional.map(usuarioMapper::fromJpaToDomain);
    }

    @Override
    public List<Usuario> findByNome(String nome) {
        return this.jpaUsuarioRepository.findByNome(nome).stream()
                .map(usuarioMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Usuario> findByTelefone(Telefone telefone) {
        return this.jpaUsuarioRepository.findByTelefone_Numero(telefone.numero()).stream()
                .map(usuarioMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.jpaUsuarioRepository.deleteById(id);
    }

    @Override
    public List<UsuarioAndPapelProjection> findUsuarioVsPapel(String username, StatusDeUsuario uStatus, StatusDePapel pStatus) {
        return this.jpaUsuarioRepository.findUsuarioVsPapel(username, uStatus, pStatus);
    }
}