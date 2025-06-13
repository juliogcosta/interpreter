package br.com.comigo.assistencia.application.usecase.prestador;

import java.util.List;
import java.util.UUID;

import com.yc.core.common.infrastructure.exception.BusinessRuleConsistencyException;
import com.yc.core.common.infrastructure.exception.ControlledException;
import com.yc.core.common.infrastructure.exception.IncompleteRegisterException;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorResponse;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;

public interface ServicoDePrestadorUseCases {
    public ServicoDePrestadorDTO create(ServicoDePrestadorDTO servicoDePrestadorDTO, Long prestadorId) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException;
    
    public void update(ServicoDePrestadorDTO servicoDePrestadorDTO, Long prestadorId) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException;

    public ServicoDePrestadorResponse getServicoDePrestadorDetailsById(UUID id, boolean full) throws ControlledException;
    
    public ServicoDePrestadorResponse getServicoDePrestadorDetailsById(UUID id, Long prestadorId, boolean full) throws ControlledException;
    
    public List<ServicoDePrestadorResponse> getAllServicoDePrestadorByPrestador(Long prestadorId, boolean full) throws ControlledException;
    
    public List<ServicoDePrestadorResponse> getServicoDePrestadorDetailsByIds(List<UUID> ids, boolean full) throws ControlledException;
    
    public List<ServicoDePrestadorResponse> getServicoDePrestadorDetailsByIds(List<UUID> ids, Long prestadorId, boolean full) throws ControlledException;
    
    public List<ServicoDePrestadorResponse> getFilteredServicoDePrestadorDetailsByStatus(Status status, Long prestadorId, boolean full) throws ControlledException;
    
    public void deleteServicoDePrestador(UUID id, Long prestadorId);
}