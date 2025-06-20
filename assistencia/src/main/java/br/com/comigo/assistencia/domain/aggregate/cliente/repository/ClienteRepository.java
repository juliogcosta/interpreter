package br.com.comigo.assistencia.domain.aggregate.cliente.repository;

import java.util.List;
import java.util.Optional;

import com.yc.core.common.model.records.Cpf;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.assistencia.domain.aggregate.cliente.Cliente;

public interface ClienteRepository {
    public Cliente create(Cliente event);

    public void update(Cliente event);

    public Optional<Cliente> findById(Long id);

    public Optional<Cliente> findByCpf(Cpf cpf);

    public List<Cliente> findByNome(String nome);

    public List<Cliente> findByTelefone(Telefone telefone);

    public void deleteById(Long id);
}