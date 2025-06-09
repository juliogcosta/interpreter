package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaPrestador;

public interface JpaPrestadorRepository extends JpaRepository<JpaPrestador, Long> {

    public Optional<JpaPrestador> findByCnpj_Cnpj(String cnpj);

    public List<JpaPrestador> findByNome(String nome);

    public List<JpaPrestador> findByTelefone_Numero(String numero);
}