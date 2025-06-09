package br.com.comigo.assistencia.application.usecase.cliente;

import java.util.List;

import br.com.comigo.assistencia.adapter.aggregate.cliente.dto.ClienteDTO;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.common.model.records.Cpf;
import br.com.comigo.common.model.records.Telefone;

public interface ClienteUseCases {
    public ClienteDTO create(ClienteDTO dto) throws IncompleteRegisterException, ControlledException;

    public void update(ClienteDTO dto) throws IncompleteRegisterException, ControlledException;

    public ClienteDTO getClienteDetailsById(Long id);

    public ClienteDTO getClienteDetailsByCpf(Cpf cpf);

    public List<ClienteDTO> getFilteredClientesByNome(String nome);

    public List<ClienteDTO> getFilteredClientesByTelefone(Telefone telefone);

    public void deleteCliente(Long id);
}
