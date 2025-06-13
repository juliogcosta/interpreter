package br.com.comigo.assistencia.adapter.aggregate.cliente.dto;

import java.util.Date;
import java.util.List;

import com.yc.core.common.model.records.Cpf;
import com.yc.core.common.model.records.Email;
import com.yc.core.common.model.records.Endereco;
import com.yc.core.common.model.records.Telefone;

public record ClienteDTO(
        Long id,
        String nome,
        Cpf cpf,
        Telefone telefone,
        Telefone whatsapp,
        Email email,
        Endereco endereco,
        Date dataNascimento,
        List<VeiculoDTO> veiculos) {
}