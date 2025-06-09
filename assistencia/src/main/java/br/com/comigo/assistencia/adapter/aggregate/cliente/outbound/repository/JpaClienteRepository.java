package br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.JpaCliente;

public interface JpaClienteRepository extends JpaRepository<JpaCliente, Long> {
    public Optional<JpaCliente> findByCpf_Cpf(String cpf);

    public List<JpaCliente> findByNome(String nome);

    public List<JpaCliente> findByTelefone_Numero(String numero);
}