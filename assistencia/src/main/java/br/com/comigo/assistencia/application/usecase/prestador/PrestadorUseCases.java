package br.com.comigo.assistencia.application.usecase.prestador;

import java.util.List;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorResponse;
import br.com.comigo.common.infrastructure.exception.BusinessRuleConsistencyException;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Telefone;

public interface PrestadorUseCases {
    public PrestadorDTO create(PrestadorDTO prestadorDTO) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException;

    public void update(PrestadorDTO prestadorDTO, Boolean isAdmin) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException;

    public PrestadorResponse getPrestadorDetailsById(Long id) throws ControlledException;

    public PrestadorResponse getPrestadorDetailsByCnpj(Cnpj cnpj) throws ControlledException;

    public List<PrestadorDTO> getFilteredPrestadorsByNome(String nome);

    public List<PrestadorDTO> getFilteredPrestadorsByTelefone(Telefone telefone);

    public void deletePrestador(Long id);
}