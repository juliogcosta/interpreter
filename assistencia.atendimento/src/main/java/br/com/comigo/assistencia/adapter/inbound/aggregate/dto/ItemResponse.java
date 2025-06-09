package br.com.comigo.assistencia.adapter.inbound.aggregate.dto;

public record ItemResponse(
    String nome,
    String unidadeDeMedida,
    Integer precoUnitario,
    Integer quantidade,
    String observacao) {
}
