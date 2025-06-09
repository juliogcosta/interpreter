package br.com.comigo.id.adapter.aggregate.usuario.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.comigo.id.adapter.aggregate.usuario.outbound.JpaPapelDeUsuario;
import br.com.comigo.id.adapter.aggregate.usuario.outbound.JpaUsuario;
import br.com.comigo.id.domain.aggregate.usuario.PapelDeUsuario;
import br.com.comigo.id.domain.aggregate.usuario.repository.PapelDeUsuarioRepository;
import br.com.comigo.id.mapper.aggregate.usuario.PapelDeUsuarioMapper;
import br.com.comigo.id.mapper.aggregate.usuario.UsuarioMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PapelDeUsuarioRepositoryImpl implements PapelDeUsuarioRepository {
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final JpaPapelDeUsuarioRepository jpaPapelDeUsuarioRepository;
    private final PapelDeUsuarioMapper papelDeUsuarioMapper;
    
    @Override
    public void create(PapelDeUsuario papelDeUsuario, Long usuarioId) {
        JpaUsuario jpaUsuario = this.jpaUsuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario não encontrado"));
        papelDeUsuario.setUsuario(this.usuarioMapper.fromJpaToDomain(jpaUsuario));

        JpaPapelDeUsuario jpaPapelDeUsuario = new JpaPapelDeUsuario();
        jpaPapelDeUsuario.setPapelId(papelDeUsuario.getPapelId());

        jpaPapelDeUsuario.setUsuario(jpaUsuario);
        jpaUsuario.getPapelDeUsuarios().add(jpaPapelDeUsuario);

        this.jpaUsuarioRepository.save(jpaUsuario);
    }

    @Override
    public Optional<PapelDeUsuario> findById(Long id) {
        Optional<JpaPapelDeUsuario> optional = this.jpaPapelDeUsuarioRepository.findById(id);
        return optional.map(papelDeUsuarioMapper::fromJpaToDomain);
    }
    
    @Override
    public List<PapelDeUsuario> findAll() {
        return this.jpaPapelDeUsuarioRepository.findAll().stream()
            .map(papelDeUsuarioMapper::fromJpaToDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        this.jpaPapelDeUsuarioRepository.deleteById(id);
    }
}