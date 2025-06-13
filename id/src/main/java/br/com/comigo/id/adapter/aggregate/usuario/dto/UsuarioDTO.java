package br.com.comigo.id.adapter.aggregate.usuario.dto;

import java.util.List;

import com.yc.core.common.model.records.Email;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.id.domain.util.StatusDeUsuario;

public record UsuarioDTO(
    Long id,
    String username,
    String password,
    String nome,
    Telefone telefone,
    Email email,
    StatusDeUsuario status,
    List<PapelDeUsuarioDTO> papelDeUsuarios
) {
}