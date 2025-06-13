package br.com.comigo.id.application.aggregate.service.usuario;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.yc.core.common.infrastructure.exception.RegisterNotFoundException;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.id.adapter.aggregate.usuario.dto.PapelDeUsuarioDTO;
import br.com.comigo.id.adapter.aggregate.usuario.dto.UsuarioDTO;
import br.com.comigo.id.adapter.aggregate.usuario.dto.UsuarioForLoginDTO;
import br.com.comigo.id.application.usecase.usuario.UsuarioUseCases;
import br.com.comigo.id.domain.aggregate.usuario.PapelDeUsuario;
import br.com.comigo.id.domain.aggregate.usuario.Usuario;
import br.com.comigo.id.domain.aggregate.usuario.repository.PapelDeUsuarioRepository;
import br.com.comigo.id.domain.aggregate.usuario.repository.UsuarioRepository;
import br.com.comigo.id.domain.projection.UsuarioAndPapelProjection;
import br.com.comigo.id.domain.util.StatusDePapel;
import br.com.comigo.id.domain.util.StatusDeUsuario;
import br.com.comigo.id.mapper.aggregate.usuario.PapelDeUsuarioMapper;
import br.com.comigo.id.mapper.aggregate.usuario.UsuarioMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioUseCases {

    private final UsuarioRepository usuarioRepository;
    private final PapelDeUsuarioRepository papelDeUsuarioRepository;
    private final UsuarioMapper clienteMapper;
    private final PapelDeUsuarioMapper papelDeUsuarioMapper;

    @Override
    public UsuarioDTO create(UsuarioDTO dto) {
        Usuario cliente = this.usuarioRepository.create(this.clienteMapper.toDomain(dto));
        return this.clienteMapper.toDto(cliente);
    }

    @Override
    public void update(UsuarioDTO dto) {
        this.usuarioRepository.update(this.clienteMapper.toDomain(dto));
    }

    @Override
    public UsuarioDTO getUsuarioDetailsById(Long id) throws RegisterNotFoundException {
        Usuario cliente = this.usuarioRepository.findById(id)
                .orElseThrow(() -> new RegisterNotFoundException("Usuario não encontrado"));
        cliente.setPassword(null);
        return this.clienteMapper.toDto(cliente);
    }

    @Override
    public UsuarioDTO getUsuarioDetailsByUsername(String username) throws RegisterNotFoundException {
        log.info(" > username: {}", username);
        Usuario usuario = this.usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RegisterNotFoundException("Usuario não encontrado"));
        usuario.setPassword(null);
        return this.clienteMapper.toDto(usuario);
    }

    @Override
    public List<UsuarioDTO> getFilteredUsuariosByNome(String nome) {
        List<UsuarioDTO> usuarioDTOs = this.usuarioRepository.findByNome(nome).stream()
                .map(clienteMapper::toDto)
                .toList().stream().map(usuarioDTO -> new UsuarioDTO(
                        usuarioDTO.id(),
                        usuarioDTO.username(),
                        null,
                        usuarioDTO.nome(),
                        usuarioDTO.telefone(),
                        usuarioDTO.email(),
                        usuarioDTO.status(),
                        usuarioDTO.papelDeUsuarios()))
                .toList();

        return usuarioDTOs;
    }

    @Override
    public List<UsuarioDTO> getFilteredUsuariosByTelefone(Telefone telefone) {
        return this.usuarioRepository.findByTelefone(telefone).stream()
                .map(clienteMapper::toDto)
                .toList().stream().map(usuarioDTO -> new UsuarioDTO(
                        usuarioDTO.id(),
                        usuarioDTO.username(),
                        null,
                        usuarioDTO.nome(),
                        usuarioDTO.telefone(),
                        usuarioDTO.email(),
                        usuarioDTO.status(),
                        usuarioDTO.papelDeUsuarios()))
                .toList();
    }

    @Override
    public void deleteUsuario(Long id) {
        this.usuarioRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void addPapelDeUsuarioToUsuario(PapelDeUsuarioDTO papelDeUsuarioDTO, Long usuarioId) {
        PapelDeUsuario papelDeUsuario = this.papelDeUsuarioMapper.toDomain(papelDeUsuarioDTO);
        this.papelDeUsuarioRepository.create(papelDeUsuario, usuarioId);
    }

    @Override
    public void deletePapelDeUsuario(Long id) {
        this.papelDeUsuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioForLoginDTO getUsuarioForLogin(String username) {
        List<UsuarioAndPapelProjection> usuarioAndPapelProjections = this.usuarioRepository.findUsuarioVsPapel(username,
                StatusDeUsuario.ATIVO, StatusDePapel.ATIVO);
        if (usuarioAndPapelProjections == null || usuarioAndPapelProjections.size() == 0) {
            return new UsuarioForLoginDTO(null, null, null, null, null, null, null);
        } else {
            List<UsuarioForLoginDTO.PapelForLoginDTO> papelForLoginDTOs = usuarioAndPapelProjections.stream()
                    .map(usuarioAndPapelProjection -> {
                        return new UsuarioForLoginDTO.PapelForLoginDTO(usuarioAndPapelProjection.getPapelNome(),
                                usuarioAndPapelProjection.getPapelStatus());
                    }).collect(Collectors.toList());
            UsuarioAndPapelProjection usuarioAndPapelProjection = usuarioAndPapelProjections.get(0);

            return new UsuarioForLoginDTO(
                    usuarioAndPapelProjection.getNome(),
                    usuarioAndPapelProjection.getUsername(),
                    usuarioAndPapelProjection.getPassword(),
                    usuarioAndPapelProjection.getEmail().getEmail(),
                    usuarioAndPapelProjection.getTelefone().getNumero(),
                    usuarioAndPapelProjection.getStatus(),
                    papelForLoginDTOs);
        }
    }
}