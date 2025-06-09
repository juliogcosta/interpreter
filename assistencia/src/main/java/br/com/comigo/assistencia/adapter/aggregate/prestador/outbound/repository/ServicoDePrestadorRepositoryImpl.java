package br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaItemDeServicoDePrestador;
import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaPrestador;
import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;
import br.com.comigo.assistencia.domain.aggregate.prestador.repository.ServicoDePrestadorRepository;
import br.com.comigo.assistencia.mapper.aggregate.prestador.ServicoDePrestadorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ServicoDePrestadorRepositoryImpl implements ServicoDePrestadorRepository {
    private final JpaPrestadorRepository jpaPrestadorRepository;
    private final JpaServicoDePrestadorRepository jpaServicoDePrestadorRepository;
    private final ServicoDePrestadorMapper servicoDePrestadorMapper;

    @Override
    public ServicoDePrestador create(ServicoDePrestador servicoDePrestador) {
    	JpaPrestador jpaPrestador = this.jpaPrestadorRepository.findById(servicoDePrestador.getPrestador().getId())
                .orElseThrow(() -> new IllegalArgumentException("Prestador não encontrado"));
    	
        JpaServicoDePrestador jpaServicoDePrestador = new JpaServicoDePrestador(servicoDePrestador);
    	jpaServicoDePrestador.setPrestador(jpaPrestador);
    	jpaPrestador.getServicoDePrestadors().add(jpaServicoDePrestador);

    	this.jpaPrestadorRepository.save(jpaPrestador);
        return servicoDePrestador;
    }
    
    @Override
    public void update(ServicoDePrestador servicoDePrestador) {
        JpaServicoDePrestador jpaServicoDePrestador = this.jpaServicoDePrestadorRepository.findById(servicoDePrestador.getId())
                .orElseThrow(() -> new IllegalArgumentException("ServicoDePrestador não encontrado"));

        jpaServicoDePrestador.setItemDeServicoDePrestadors(servicoDePrestador.getItemDeServicoDePrestadors().stream()
        		.map(itemDeServicoDePrestador -> {
        			return new JpaItemDeServicoDePrestador(
        					itemDeServicoDePrestador.getNome(),
        					itemDeServicoDePrestador.getUnidadeDeMedida(),
        					itemDeServicoDePrestador.getPrecoUnitario());
        		}).toList());
        jpaServicoDePrestador.setStatus(servicoDePrestador.getStatus());
        
        this.jpaServicoDePrestadorRepository.save(jpaServicoDePrestador);
    }

    @Override
    public Optional<ServicoDePrestador> findById(UUID uuid) {
    	Optional<JpaServicoDePrestador> optional = this.jpaServicoDePrestadorRepository.findById(uuid);
        return optional.map(this.servicoDePrestadorMapper::fromJpaToDomain);
    }

    @Override
    public List<ServicoDePrestador> findByPrestador(Prestador prestador) {
    	JpaPrestador jpaPrestador = jpaPrestadorRepository.findById(prestador.getId())
    	        .orElseThrow(() -> new IllegalArgumentException("Prestador não encontrado"));
    	
        return this.jpaServicoDePrestadorRepository.findByPrestador(jpaPrestador).stream()
                .map(this.servicoDePrestadorMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServicoDePrestador> findAllByIdIn(List<UUID> ids) {
    	
        return this.jpaServicoDePrestadorRepository.findAllByIdIn(ids).stream()
                .map(this.servicoDePrestadorMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServicoDePrestador> findByStatus(Status status) {
        return this.jpaServicoDePrestadorRepository.findByStatus(status).stream()
                .map(this.servicoDePrestadorMapper::fromJpaToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        this.jpaServicoDePrestadorRepository.deleteById(id);
    }
}