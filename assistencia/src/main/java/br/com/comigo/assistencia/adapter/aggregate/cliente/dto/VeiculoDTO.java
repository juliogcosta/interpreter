package br.com.comigo.assistencia.adapter.aggregate.cliente.dto;

public record VeiculoDTO(
        String marca,
        String modelo,
        String placa,
        Boolean validado) {
}