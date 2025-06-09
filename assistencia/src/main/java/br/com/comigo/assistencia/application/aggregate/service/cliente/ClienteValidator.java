package br.com.comigo.assistencia.application.aggregate.service.cliente;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import br.com.comigo.assistencia.domain.aggregate.cliente.Cliente;
import br.com.comigo.assistencia.domain.aggregate.cliente.Veiculo;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ClienteValidator {
	@SuppressWarnings("deprecation")
	protected ClienteValidator requiredFieldsFor(Cliente cliente) throws IncompleteRegisterException {
		if (cliente.getNome() == null) {
        	throw new IncompleteRegisterException("Faltando Nome.");
        } else if (cliente.getNome().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Nome.");
        } else if (cliente.getNome().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Nome.");
        }
        
        if (cliente.getCpf() == null) {
        	throw new IncompleteRegisterException("Faltando CPF.");
        } else if (cliente.getCpf().cpf() == null) {
        	throw new IncompleteRegisterException("Faltando CPF.");
        } else if (cliente.getCpf().cpf().isBlank()) {
        	throw new IncompleteRegisterException("Faltando CPF.");
        } else if (cliente.getCpf().cpf().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando CPF.");
        } 
        
        if (cliente.getTelefone() == null) {
        	throw new IncompleteRegisterException("Faltando Telefone.");
        } else if (cliente.getTelefone().numero() == null) {
        	throw new IncompleteRegisterException("Faltando Telefone.numero.");
        } else if (cliente.getTelefone().numero().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Telefone.");
        } else if (cliente.getTelefone().numero().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Telefone.");
        } else if (cliente.getTelefone().numero().length() < 7 
        		&& cliente.getTelefone().numero().length() > 16) {
        	throw new IncompleteRegisterException("Telefone parece inálido.");
        }
        
        if (cliente.getDataNascimento() == null) {
        	throw new IncompleteRegisterException("Faltando DataNascimento.");
        } else if (cliente.getDataNascimento().getYear() + 18 > new Date().getYear()) {
        	throw new IncompleteRegisterException("A DataNascimento parece inválida.");
        }
        
        if (cliente.getEndereco() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.");
        } else if (cliente.getEndereco().logradouro() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.logradouro.");
        } else if (cliente.getEndereco().logradouro().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.logradouro.");
        } else if (cliente.getEndereco().logradouro().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.logradouro.");
        } else if (cliente.getEndereco().numero() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.numero.");
        } else if (cliente.getEndereco().numero().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.numero.");
        } else if (cliente.getEndereco().numero().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.numero.");
        } else if (cliente.getEndereco().bairro() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.bairro.");
        } else if (cliente.getEndereco().bairro().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.bairro.");
        } else if (cliente.getEndereco().bairro().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.bairro.");
        } else if (cliente.getEndereco().cidade() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.cidade.");
        } else if (cliente.getEndereco().cidade().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cidade.");
        } else if (cliente.getEndereco().cidade().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cidade.");
        } else if (cliente.getEndereco().estado() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.estado.");
        } else if (cliente.getEndereco().estado().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.estado.");
        } else if (cliente.getEndereco().estado().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.estado.");
        } else if (cliente.getEndereco().cep() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.cep.");
        } else if (cliente.getEndereco().cep().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cep.");
        } else if (cliente.getEndereco().cep().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cep.");
        }
        
        return this;
	}
	
	protected void requiredFieldsFor(List<Veiculo> veiculos) throws IncompleteRegisterException {
		for (Veiculo veiculo : veiculos) {
			if (veiculo.getPlaca() == null) {
	        	throw new IncompleteRegisterException("Faltando veiculo.placa.");
			} else if (veiculo.getPlaca().length() < 7) {
	        	throw new IncompleteRegisterException("o valor veiculo.placa parece inválido.");
			} else if (veiculo.getPlaca().length() > 8) {
	        	throw new IncompleteRegisterException("o valor veiculo.placa parece inválido.");
			}
		}
	}
	
	protected void requiredFieldsFor(Veiculo veiculo) throws IncompleteRegisterException {
		if (veiculo.getPlaca() == null) {
        	throw new IncompleteRegisterException("Faltando veiculo.placa.");
		} else if (veiculo.getPlaca().length() < 7) {
        	throw new IncompleteRegisterException("o valor veiculo.placa parece inválido.");
		} else if (veiculo.getPlaca().length() > 8) {
        	throw new IncompleteRegisterException("o valor veiculo.placa parece inválido.");
		}
	}
}


























