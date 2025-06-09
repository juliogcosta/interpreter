package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaPrestador;
import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;

public interface JpaServicoDePrestadorRepository extends JpaRepository<JpaServicoDePrestador, UUID> {

    public Optional<JpaServicoDePrestador> findById(UUID id);
    
    public List<JpaServicoDePrestador> findAllByIdIn(List<UUID> ids);

    public List<JpaServicoDePrestador> findByPrestador(JpaPrestador jpaPrestador);

    public List<JpaServicoDePrestador> findByStatus(Status status);

    public void deleteById(UUID id);
}