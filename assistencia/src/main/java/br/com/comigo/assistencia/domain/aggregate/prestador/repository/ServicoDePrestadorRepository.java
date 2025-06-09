package br.com.comigo.assistencia.domain.aggregate.prestador.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;

public interface ServicoDePrestadorRepository {
    public ServicoDePrestador create(ServicoDePrestador servicoDePrestador);

    public void update(ServicoDePrestador servicoDePrestador);

    public Optional<ServicoDePrestador> findById(UUID id);

    public List<ServicoDePrestador> findAllByIdIn(List<UUID> ids);

    public List<ServicoDePrestador> findByPrestador(Prestador prestador);

    public List<ServicoDePrestador> findByStatus(Status status);

    public void deleteById(UUID id);
}