package br.com.comigo.assistencia.application.aggregate.service.prestador;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorResponse;
import br.com.comigo.assistencia.application.usecase.prestador.PrestadorUseCases;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.repository.PrestadorRepository;
import br.com.comigo.assistencia.mapper.aggregate.prestador.PrestadorMapper;
import br.com.comigo.common.infrastructure.exception.BusinessRuleConsistencyException;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Telefone;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrestadorServiceImpl implements PrestadorUseCases {

    private final PrestadorRepository prestadorRepository;
    private final PrestadorMapper prestadorMapper;
    private final PrestadorValidator prestadorValidator;

    @Override
    public PrestadorDTO create(@Nonnull PrestadorDTO prestadorDTO) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
        Prestador prestador = this.prestadorMapper.toDomain(prestadorDTO);
        prestador.setId(null);
        this.prestadorValidator.requiredFieldsFor(prestador);
        prestador = this.prestadorRepository.create(prestador);
        return this.prestadorMapper.toDto(prestador);
    }

    @Override
    public void update(@Nonnull PrestadorDTO prestadorDTO, Boolean isAdmin) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
    	Prestador prestador = this.prestadorMapper.toDomain(prestadorDTO);
        this.prestadorValidator.requiredFieldsFor(prestador);
        this.prestadorRepository.update(prestador, isAdmin);
    }

    @Override
    public PrestadorResponse getPrestadorDetailsById(@Nonnull Long id) throws ControlledException {
        Prestador prestador = this.prestadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestador not found"));

        PrestadorResponse prestadorResponse = this.prestadorMapper.toResponse(prestador);
        return prestadorResponse;
    }

    @Override
    public PrestadorResponse getPrestadorDetailsByCnpj(@Nonnull Cnpj cnpj) throws ControlledException {
        Prestador prestador = this.prestadorRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new RuntimeException("Prestador not found"));
        
        PrestadorResponse prestadorResponse = this.prestadorMapper.toResponse(prestador);
        return prestadorResponse;
    }

    @Override
    public List<PrestadorDTO> getFilteredPrestadorsByNome(@Nonnull String nome) {
        return this.prestadorRepository.findByNome(nome).stream()
                .map(this.prestadorMapper::toDto)
                .toList();
    }

    @Override
    public List<PrestadorDTO> getFilteredPrestadorsByTelefone(@Nonnull Telefone telefone) {
        return this.prestadorRepository.findByTelefone(telefone).stream()
                .map(this.prestadorMapper::toDto)
                .toList();
    }

    @Override
    public void deletePrestador(Long id) {
        this.prestadorRepository.deleteById(id);
    }
}