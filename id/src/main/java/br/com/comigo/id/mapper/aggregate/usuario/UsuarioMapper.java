package br.com.comigo.id.mapper.aggregate.usuario;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.comigo.common.model.records.Email;
import br.com.comigo.common.model.records.Telefone;
import br.com.comigo.id.adapter.aggregate.usuario.dto.PapelDeUsuarioDTO;
import br.com.comigo.id.adapter.aggregate.usuario.dto.UsuarioDTO;
import br.com.comigo.id.adapter.aggregate.usuario.outbound.JpaUsuario;
import br.com.comigo.id.domain.aggregate.usuario.PapelDeUsuario;
import br.com.comigo.id.domain.aggregate.usuario.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

        @Mappings({ @Mapping(source = "usuarioDTO.id", target = "id"),
                        @Mapping(source = "usuarioDTO.nome", target = "nome"),
                        @Mapping(source = "usuarioDTO.username", target = "username"),
                        @Mapping(source = "usuarioDTO.password", target = "password"),
                        @Mapping(source = "usuarioDTO.telefone", target = "telefone"),
                        @Mapping(source = "usuarioDTO.email", target = "email"),
                        @Mapping(source = "usuarioDTO.status", target = "status"),
                        @Mapping(target = "papelDeUsuarios", ignore = true) })
        Usuario toDomain(UsuarioDTO usuarioDTO);

        @Mappings({ @Mapping(source = "usuario.id", target = "id"), @Mapping(source = "usuario.nome", target = "nome"),
                        @Mapping(source = "usuario.username", target = "username"),
                        @Mapping(source = "usuario.password", target = "password"),
                        @Mapping(source = "usuario.telefone", target = "telefone"),
                        @Mapping(source = "usuario.email", target = "email"),
                        @Mapping(source = "usuario.status", target = "status"), })
        UsuarioDTO toDto(Usuario usuario);

        default Usuario fromJpaToDomain(JpaUsuario jpaUsuario) {
                if (jpaUsuario == null) {
                        return null;
                }

                Telefone telefone = new Telefone(jpaUsuario.getTelefone().getNumero(),
                                jpaUsuario.getTelefone().getTipo());

                Email email = new Email(jpaUsuario.getEmail().getEmail());

                List<PapelDeUsuario> papelDeUsuarios = new ArrayList<>();
                if (jpaUsuario.getPapelDeUsuarios() == null) {

                } else {
                        papelDeUsuarios = jpaUsuario.getPapelDeUsuarios().stream()
                                        .map(veiculo -> new PapelDeUsuario(veiculo.getId(), veiculo.getPapelId()))
                                        .toList();
                }

                Usuario usuario = new Usuario(jpaUsuario.getId(), jpaUsuario.getUsername(), jpaUsuario.getNome(),
                                telefone, email, jpaUsuario.getStatus());
                usuario.setPapelDeUsuarios(papelDeUsuarios);

                return usuario;
        }

        default UsuarioDTO fromJpaToDto(JpaUsuario jpaUsuario) {
                if (jpaUsuario == null) {
                        return null;
                }

                Telefone telefone = new Telefone(jpaUsuario.getTelefone().getNumero(),
                                jpaUsuario.getTelefone().getTipo());

                Email email = new Email(jpaUsuario.getEmail().getEmail());

                List<PapelDeUsuarioDTO> papelDeUsuarios = new ArrayList<>();
                if (jpaUsuario.getPapelDeUsuarios() == null) {

                } else
                        papelDeUsuarios = jpaUsuario.getPapelDeUsuarios().stream()
                                        .map(veiculo -> new PapelDeUsuarioDTO(veiculo.getId(), veiculo.getPapelId()))
                                        .toList();

                return new UsuarioDTO(jpaUsuario.getId(), jpaUsuario.getUsername(), jpaUsuario.getPassword(),
                                jpaUsuario.getNome(), telefone, email, jpaUsuario.getStatus(), papelDeUsuarios);
        }

}
