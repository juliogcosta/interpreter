package br.com.comigo.assistencia.application.usecase.prestador;

import java.util.List;

import com.yc.core.common.infrastructure.exception.BusinessRuleConsistencyException;
import com.yc.core.common.infrastructure.exception.ControlledException;
import com.yc.core.common.infrastructure.exception.IncompleteRegisterException;
import com.yc.core.common.model.records.Cnpj;
import com.yc.core.common.model.records.Telefone;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorResponse;

public interface PrestadorUseCases {
    public PrestadorDTO create(PrestadorDTO prestadorDTO) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException;

    public void update(PrestadorDTO prestadorDTO, Boolean isAdmin) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException;

    public PrestadorResponse getPrestadorDetailsById(Long id) throws ControlledException;

    public PrestadorResponse getPrestadorDetailsByCnpj(Cnpj cnpj) throws ControlledException;

    public List<PrestadorDTO> getFilteredPrestadorsByNome(String nome);

    public List<PrestadorDTO> getFilteredPrestadorsByTelefone(Telefone telefone);

    public void deletePrestador(Long id);
}