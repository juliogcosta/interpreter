package br.com.comigo.assistencia.application.aggregate.service.prestador;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.yc.core.common.infrastructure.exception.BusinessRuleConsistencyException;
import com.yc.core.common.infrastructure.exception.ControlledException;
import com.yc.core.common.infrastructure.exception.IncompleteRegisterException;
import com.yc.core.common.model.records._assistencia.ServicoDTO;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorResponse;
import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.restclient.AssistenciaCatalogoService;
import br.com.comigo.assistencia.application.usecase.prestador.ServicoDePrestadorUseCases;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador.Status;
import br.com.comigo.assistencia.domain.aggregate.prestador.repository.ServicoDePrestadorRepository;
import br.com.comigo.assistencia.mapper.aggregate.prestador.ServicoDePrestadorMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicoDePrestadorServiceImpl implements ServicoDePrestadorUseCases {

    private final ServicoDePrestadorRepository servicoDePrestadorRepository;
    private final ServicoDePrestadorMapper servicoDePrestadorMapper;
    private final PrestadorValidator prestadorValidator;
    private final AssistenciaCatalogoService assistenciaCatalogoService;

    @Override
    public ServicoDePrestadorDTO create(ServicoDePrestadorDTO servicoDePrestadorDTO, Long prestadorId) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
        ServicoDePrestador servicoDePrestador = this.servicoDePrestadorMapper.toDomain(servicoDePrestadorDTO);
        this.prestadorValidator.requiredFieldsFor(servicoDePrestador);
        if (servicoDePrestadorDTO.itemDeServicoDePrestadors() == null) {
        	
        } else {
        	this.prestadorValidator._requiredFieldsFor(servicoDePrestador.getItemDeServicoDePrestadors());
        	
        	List<ServicoDTO> servicoDTOs = this.assistenciaCatalogoService.findAllServicosByCertificateIso();
        	List<UUID> idDeServicoDTOs = servicoDTOs.stream().map(servicoDTO -> servicoDTO.uuid()).toList();
        	if (idDeServicoDTOs.contains(servicoDePrestador.getId())) {
        		
        	} else throw new BusinessRuleConsistencyException("Falta certificacao ISO.");
        }
        
        servicoDePrestador.setPrestador(new Prestador());
        servicoDePrestador.getPrestador().setId(prestadorId);
        
        return this.servicoDePrestadorMapper.toDto(this.servicoDePrestadorRepository.create(servicoDePrestador));
    }

    @Override
    public void update(ServicoDePrestadorDTO servicoDePrestadorDTO, Long prestadorId) throws IncompleteRegisterException, BusinessRuleConsistencyException, ControlledException {
    	ServicoDePrestador servicoDePrestador = this.servicoDePrestadorMapper.toDomain(servicoDePrestadorDTO);
        this.prestadorValidator.requiredFieldsFor(servicoDePrestador);
        if (servicoDePrestadorDTO.itemDeServicoDePrestadors() == null) {
        	
        } else {
        	this.prestadorValidator._requiredFieldsFor(servicoDePrestador.getItemDeServicoDePrestadors());
        	
        	List<ServicoDTO> servicoDTOs = this.assistenciaCatalogoService.findAllServicosByCertificateIso();
        	log.info(" > servicoDTOs: {}", servicoDTOs);
        	if (servicoDTOs.stream()
        			.filter(servicoDTO -> servicoDTO.uuid().toString().equals(servicoDePrestador.getId().toString()))
        			.map(servicoDTO -> servicoDTO.uuid())
        			.toList().isEmpty()) {
        		throw new BusinessRuleConsistencyException("Falta certificacao ISO.");
        	}
        }
        
        servicoDePrestador.setPrestador(new Prestador());
        servicoDePrestador.getPrestador().setId(prestadorId);
        
        this.servicoDePrestadorRepository.update(servicoDePrestador);
    }

    @Override
    public List<ServicoDePrestadorResponse> getAllServicoDePrestadorByPrestador(Long prestadorId, boolean full) throws ControlledException {
        Prestador prestador = new Prestador();
        prestador.setId(prestadorId);
    	return this.servicoDePrestadorRepository.findByPrestador(prestador).stream()
                .map(servicoDePrestador -> {
                    ServicoDTO servicoDTO = null;
					try {
						servicoDTO = this.assistenciaCatalogoService.findServicoById(servicoDePrestador.getId().toString());
					} catch (ControlledException e) {
						throw new RuntimeException(e);
					}
                    if (servicoDTO == null) {
                    	throw new RuntimeException(new ControlledException("Falha ao recuperar o agregado servico."));
                    }
                	servicoDePrestador.setNome(servicoDTO.nome());
                	return this.servicoDePrestadorMapper.fromDomainToResponse(servicoDePrestador, full);
                })
                .toList();
    }

    @Override
    public ServicoDePrestadorResponse getServicoDePrestadorDetailsById(UUID id, boolean full) throws ControlledException {
        return this.getServicoDePrestadorDetailsById(id, null, full);
    }

    @Override
    public ServicoDePrestadorResponse getServicoDePrestadorDetailsById(UUID id, Long prestadorId, boolean full) throws ControlledException {
        ServicoDePrestador servicoDePrestador = this.servicoDePrestadorRepository.findById(id)
        		.orElseThrow(() -> new ControlledException("ServicoDePrestador não encontrado."));
		if (servicoDePrestador.getPrestador().getId() == prestadorId) {
		        	
		}// else throw new ControlledException("Acesso a dado negado.");
        
        ServicoDTO servicoDTO = null;
		try {
			servicoDTO = this.assistenciaCatalogoService.findServicoById(servicoDePrestador.getId().toString());
		} catch (ControlledException e) {
			throw new RuntimeException(e);
		}
        if (servicoDTO == null) {
        	throw new RuntimeException(new ControlledException("Falha ao recuperar o agregado serviço."));
        }
    	servicoDePrestador.setNome(servicoDTO.nome());
    	return this.servicoDePrestadorMapper.fromDomainToResponse(servicoDePrestador, full);
    }

    @Override
    public List<ServicoDePrestadorResponse> getServicoDePrestadorDetailsByIds(List<UUID> ids, boolean full) throws ControlledException {
        return this.getServicoDePrestadorDetailsByIds(ids, null, full);
    }

    @Override
    public List<ServicoDePrestadorResponse> getServicoDePrestadorDetailsByIds(List<UUID> ids, Long prestadorId, boolean full) throws ControlledException {
        return this.servicoDePrestadorRepository.findAllByIdIn(ids).stream().map(servicoDePrestador -> {
        	try {
        		if (servicoDePrestador.getPrestador().getId() == prestadorId) {
                	
                } //else throw new ControlledException("Acesso a dado negado.");
                
                ServicoDTO servicoDTO = null;
				try {
					servicoDTO = this.assistenciaCatalogoService.findServicoById(servicoDePrestador.getId().toString());
				} catch (ControlledException e) {
					throw new RuntimeException(e);
				}
                if (servicoDTO == null) {
                	throw new RuntimeException(new ControlledException("Falha ao recuperar o agregado serviço."));
                }
            	servicoDePrestador.setNome(servicoDTO.nome());
            	return this.servicoDePrestadorMapper.fromDomainToResponse(servicoDePrestador, full);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
        }).toList();
    }

    @Override
    public List<ServicoDePrestadorResponse> getFilteredServicoDePrestadorDetailsByStatus(Status status, Long prestadorId, boolean full) throws ControlledException {
        return this.servicoDePrestadorRepository.findByStatus(status).stream()
                .map(servicoDePrestador -> {
                    if (servicoDePrestador.getPrestador().getId() == prestadorId) {
                    	
                    } // else throw new RuntimeException(new ControlledException("Acesso a dado negado."));
                    
                    ServicoDTO servicoDTO = null;
					try {
						servicoDTO = this.assistenciaCatalogoService.findServicoById(servicoDePrestador.getId().toString());
					} catch (ControlledException e) {
						throw new RuntimeException(e);
					}
                    if (servicoDTO == null) {
                    	throw new RuntimeException(new ControlledException("Falha ao recuperar o agregado serviço."));
                    }
                	servicoDePrestador.setNome(servicoDTO.nome());
                	return this.servicoDePrestadorMapper.fromDomainToResponse(servicoDePrestador, full);
                })
                .toList();
    }

    @Override
    public void deleteServicoDePrestador(UUID id, Long prestadorId) {
        this.servicoDePrestadorRepository.deleteById(id);
    }
}