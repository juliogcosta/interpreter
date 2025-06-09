package br.com.comigo.assistencia.domain.aggregate.prestador.repository;

import java.util.List;
import java.util.Optional;

import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Telefone;

public interface PrestadorRepository {
    public Prestador create(Prestador prestador);

    public void update(Prestador prestador, Boolean isAdmin);

    public Optional<Prestador> findById(Long id);

    public Optional<Prestador> findByCnpj(Cnpj cnpj);

    public List<Prestador> findByNome(String nome);

    public List<Prestador> findByTelefone(Telefone telefone);

    public void deleteById(Long id);
}