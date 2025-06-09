package br.com.comigo.assistencia.application.aggregate.service.cliente;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.comigo.assistencia.adapter.aggregate.cliente.dto.ClienteDTO;
import br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.restclient.DETRANService;
import br.com.comigo.assistencia.application.usecase.cliente.ClienteUseCases;
import br.com.comigo.assistencia.domain.aggregate.cliente.Cliente;
import br.com.comigo.assistencia.domain.aggregate.cliente.Veiculo;
import br.com.comigo.assistencia.domain.aggregate.cliente.repository.ClienteRepository;
import br.com.comigo.assistencia.mapper.aggregate.cliente.ClienteMapper;
import br.com.comigo.common.infrastructure.exception.ControlledException;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import br.com.comigo.common.model.records.Cpf;
import br.com.comigo.common.model.records.Telefone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteUseCases {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteValidator clienteValidator;
    private final DETRANService detranService;

    @Override
    public ClienteDTO create(ClienteDTO clienteDTO) throws IncompleteRegisterException, ControlledException {
        Cliente cliente = this.clienteMapper.toDomain(clienteDTO);
        this.clienteValidator.requiredFieldsFor(cliente);
        if (clienteDTO.veiculos() == null) {
        	
        } else {
        	cliente.setVeiculos(clienteDTO.veiculos().stream().map(veiculoDTO -> {
        		try {
            		Veiculo veiculo = new Veiculo(
            				veiculoDTO.marca(), 
            				veiculoDTO.modelo(), 
            				veiculoDTO.placa());
					veiculo.setValidado(this.detranService.consultarSituacaoDeVeiculo(veiculoDTO.placa()));

	        		return veiculo;
				} catch (ControlledException e) {
					throw new RuntimeException(e);
				}
        	}).toList());
        	
        	this.clienteValidator.requiredFieldsFor(cliente.getVeiculos());
        }
        
        return this.clienteMapper.toDto(this.clienteRepository.create(cliente));
    }

    @Override
    public void update(ClienteDTO clienteDTO) throws IncompleteRegisterException, ControlledException {
    	Cliente cliente = this.clienteMapper.toDomain(clienteDTO);
        this.clienteValidator.requiredFieldsFor(cliente);
        if (clienteDTO.veiculos() == null) {
        	
        } else {
        	cliente.setVeiculos(clienteDTO.veiculos().stream().map(veiculoDTO -> {
        		try {
            		Veiculo veiculo = new Veiculo(
            				veiculoDTO.marca(), 
            				veiculoDTO.modelo(), 
            				veiculoDTO.placa());
					veiculo.setValidado(this.detranService.consultarSituacaoDeVeiculo(veiculoDTO.placa()));

	        		return veiculo;
				} catch (ControlledException e) {
					throw new RuntimeException(e);
				}
        	}).toList());
        	this.clienteValidator.requiredFieldsFor(cliente.getVeiculos());
        }
        this.clienteRepository.update(cliente);
    }

    @Override
    public ClienteDTO getClienteDetailsById(Long id) {
        Cliente cliente = this.clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente not found"));
        return this.clienteMapper.toDto(cliente);
    }

    @Override
    public ClienteDTO getClienteDetailsByCpf(Cpf cpf) {
        Cliente cliente = this.clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Cliente not found"));
        return this.clienteMapper.toDto(cliente);
    }

    @Override
    public List<ClienteDTO> getFilteredClientesByNome(String nome) {
        return this.clienteRepository.findByNome(nome).stream()
                .map(clienteMapper::toDto)
                .toList();
    }

    @Override
    public List<ClienteDTO> getFilteredClientesByTelefone(Telefone telefone) {
        return this.clienteRepository.findByTelefone(telefone).stream()
                .map(clienteMapper::toDto)
                .toList();
    }

    @Override
    public void deleteCliente(Long id) {
        this.clienteRepository.deleteById(id);
    }
}