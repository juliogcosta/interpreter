package br.com.comigo.assistencia.application.aggregate.service.prestador;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.comigo.assistencia.domain.aggregate.prestador.ItemDeServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador;
import br.com.comigo.common.infrastructure.exception.IncompleteRegisterException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PrestadorValidator {
	protected PrestadorValidator requiredFieldsFor(Prestador prestador) throws IncompleteRegisterException {
		log.info(" > prestador: {}", prestador);
		
		if (prestador.getNome() == null) {
        	throw new IncompleteRegisterException("Faltando Nome.");
        } else if (prestador.getNome().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Nome.");
        } else if (prestador.getNome().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Nome.");
        }
        
        if (prestador.getCnpj() == null) {
        	throw new IncompleteRegisterException("Faltando CNPJ.");
        } else if (prestador.getCnpj().cnpj() == null) {
        	throw new IncompleteRegisterException("Faltando CNPJ.");
        } else if (prestador.getCnpj().cnpj().isBlank()) {
        	throw new IncompleteRegisterException("Faltando CNPJ.");
        } else if (prestador.getCnpj().cnpj().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando CNPJ.");
        } else /*if (new CNPJ().validar(prestador.getCnpj().cnpj())) {
        	throw new IncompleteRegisterException("CNPJ invlálido.");
        }*/
        
        if (prestador.getTelefone() == null) {
        	throw new IncompleteRegisterException("Faltando Telefone.");
        } else if (prestador.getTelefone().numero() == null) {
        	throw new IncompleteRegisterException("Faltando Telefone.numero.");
        } else if (prestador.getTelefone().numero().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Telefone.");
        } else if (prestador.getTelefone().numero().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Telefone.");
        }/*else if (prestador.getTelefone().numero().length() < 7 
        		&& prestador.getTelefone().numero().length() > 16) {
        	throw new IncompleteRegisterException("Telefone parece inálido.");
        }*/
        
        if (prestador.getEndereco() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.");
        } else if (prestador.getEndereco().logradouro() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.logradouro.");
        } else if (prestador.getEndereco().logradouro().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.logradouro.");
        } else if (prestador.getEndereco().logradouro().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.logradouro.");
        } else if (prestador.getEndereco().numero() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.numero.");
        } else if (prestador.getEndereco().numero().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.numero.");
        } else if (prestador.getEndereco().numero().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.numero.");
        } else if (prestador.getEndereco().bairro() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.bairro.");
        } else if (prestador.getEndereco().bairro().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.bairro.");
        } else if (prestador.getEndereco().bairro().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.bairro.");
        } else if (prestador.getEndereco().cidade() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.cidade.");
        } else if (prestador.getEndereco().cidade().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cidade.");
        } else if (prestador.getEndereco().cidade().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cidade.");
        } else if (prestador.getEndereco().estado() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.estado.");
        } else if (prestador.getEndereco().estado().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.estado.");
        } else if (prestador.getEndereco().estado().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.estado.");
        } else if (prestador.getEndereco().cep() == null) {
        	throw new IncompleteRegisterException("Faltando Endereco.cep.");
        } else if (prestador.getEndereco().cep().isBlank()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cep.");
        } else if (prestador.getEndereco().cep().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando Endereco.cep.");
        }/*else if (prestador.getEndereco().cep().length() != 9) {
        	throw new IncompleteRegisterException("Endereco.cep. inválido");
        }*/
        
        if (prestador.getStatus() == null) {
        	throw new IncompleteRegisterException("Faltando Status.");
        }
        
        return this;
	}
	
	protected void requiredFieldsFor(List<ServicoDePrestador> servicoDePrestadors) throws IncompleteRegisterException {
		for (ServicoDePrestador servicoDePrestador : servicoDePrestadors) {
			if (servicoDePrestador.getId() == null) {
	        	throw new IncompleteRegisterException("Faltando ServicoDePrestador.uuid.");
			} else if (servicoDePrestador.getStatus() == null) {
	        	throw new IncompleteRegisterException("Faltando ServicoDePrestador.Status.");
			} 
		}
	}
	
	protected void requiredFieldsFor(ServicoDePrestador servicoDePrestador) throws IncompleteRegisterException {
		if (servicoDePrestador.getId() == null) {
        	throw new IncompleteRegisterException("Faltando ServicoDePrestador.uuid.");
		} else if (servicoDePrestador.getStatus() == null) {
        	throw new IncompleteRegisterException("Faltando ServicoDePrestador.Status.");
		} 
	}

	protected void _requiredFieldsFor(List<ItemDeServicoDePrestador> itemDeServicoDePrestadors) throws IncompleteRegisterException {
		for (ItemDeServicoDePrestador itemDeServicoDePrestador : itemDeServicoDePrestadors) {
			if (itemDeServicoDePrestador.getPrecoUnitario() == null) {
	        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.precoUnitario.");
			} else if (itemDeServicoDePrestador.getPrecoUnitario() < 0) {
	        	throw new IncompleteRegisterException("O do itemDeServicoDePrestador.precoUnitario nao pode se inferior a 0.");
			} else if (itemDeServicoDePrestador.getUnidadeDeMedida() == null) {
	        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.unidadeDeMedida.");
			} else if (itemDeServicoDePrestador.getUnidadeDeMedida().isEmpty()) {
	        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.unidadeDeMedida.");
			} else if (itemDeServicoDePrestador.getUnidadeDeMedida().isBlank()) {
	        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.unidadeDeMedida.");
			}
		} 
	}
	
	protected void requiredFieldsFor(ItemDeServicoDePrestador itemDeServicoDePrestador) throws IncompleteRegisterException {
		if (itemDeServicoDePrestador.getPrecoUnitario() == null) {
        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.precoUnitario.");
		} else if (itemDeServicoDePrestador.getPrecoUnitario() < 0) {
        	throw new IncompleteRegisterException("O do itemDeServicoDePrestador.precoUnitario nao pode se inferior a 0.");
		} else if (itemDeServicoDePrestador.getUnidadeDeMedida() == null) {
        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.unidadeDeMedida.");
		} else if (itemDeServicoDePrestador.getUnidadeDeMedida().isEmpty()) {
        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.unidadeDeMedida.");
		} else if (itemDeServicoDePrestador.getUnidadeDeMedida().isBlank()) {
        	throw new IncompleteRegisterException("Faltando itemDeServicoDePrestador.unidadeDeMedida.");
		} 
	}
}


























