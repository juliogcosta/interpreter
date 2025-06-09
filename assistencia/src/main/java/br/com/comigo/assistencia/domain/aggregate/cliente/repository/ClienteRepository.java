package br.com.comigo.assistencia.domain.aggregate.cliente.repository;

import java.util.List;
import java.util.Optional;

import br.com.comigo.assistencia.domain.aggregate.cliente.Cliente;
import br.com.comigo.common.model.records.Cpf;
import br.com.comigo.common.model.records.Telefone;

public interface ClienteRepository {
    public Cliente create(Cliente event);

    public void update(Cliente event);

    public Optional<Cliente> findById(Long id);

    public Optional<Cliente> findByCpf(Cpf cpf);

    public List<Cliente> findByNome(String nome);

    public List<Cliente> findByTelefone(Telefone telefone);

    public void deleteById(Long id);
}