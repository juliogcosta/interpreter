package br.com.comigo.id.adapter.aggregate.papel.dto;

import br.com.comigo.id.domain.util.StatusDePapel;

public record PapelDTO(
                Long id,
                String nome,
                StatusDePapel status) {
}