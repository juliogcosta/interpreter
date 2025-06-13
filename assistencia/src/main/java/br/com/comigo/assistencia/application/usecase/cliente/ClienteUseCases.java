package br.com.comigo.assistencia.application.usecase.cliente;

import java.util.List;

import com.yc.core.common.infrastructure.exception.ControlledException;
import com.yc.core.common.infrastructure.exception.IncompleteRegisterException;
import com.yc.core.common.model.records.Cpf;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.assistencia.adapter.aggregate.cliente.dto.ClienteDTO;

public interface ClienteUseCases {
    public ClienteDTO create(ClienteDTO dto) throws IncompleteRegisterException, ControlledException;

    public void update(ClienteDTO dto) throws IncompleteRegisterException, ControlledException;

    public ClienteDTO getClienteDetailsById(Long id);

    public ClienteDTO getClienteDetailsByCpf(Cpf cpf);

    public List<ClienteDTO> getFilteredClientesByNome(String nome);

    public List<ClienteDTO> getFilteredClientesByTelefone(Telefone telefone);

    public void deleteCliente(Long id);
}
