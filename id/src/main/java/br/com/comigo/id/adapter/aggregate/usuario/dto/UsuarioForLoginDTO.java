package br.com.comigo.id.adapter.aggregate.usuario.dto;

import java.util.List;

public record UsuarioForLoginDTO(
    String nome,
    String username,
    String password,
    String email,
    String telefone,
    String status,
    List<PapelForLoginDTO> papeis
) {
    public record PapelForLoginDTO (
        String nome,
        String status
    ) {}
}