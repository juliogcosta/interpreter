package br.com.comigo.assistencia.mapper.aggregate.cliente;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.slf4j.LoggerFactory;

import br.com.comigo.assistencia.adapter.aggregate.cliente.dto.ClienteDTO;
import br.com.comigo.assistencia.adapter.aggregate.cliente.dto.VeiculoDTO;
import br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.JpaCliente;
import br.com.comigo.assistencia.domain.aggregate.cliente.Cliente;
import br.com.comigo.assistencia.domain.aggregate.cliente.Veiculo;
import br.com.comigo.common.model.records.Cpf;
import br.com.comigo.common.model.records.Email;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

	@Mappings({ 
			@Mapping(source = "clienteDTO.id", target = "id"),
			@Mapping(source = "clienteDTO.nome", target = "nome"), 
			@Mapping(source = "clienteDTO.cpf", target = "cpf"),
			@Mapping(source = "clienteDTO.telefone", target = "telefone"),
			@Mapping(source = "clienteDTO.whatsapp", target = "whatsapp"),
			@Mapping(source = "clienteDTO.email", target = "email"),
			@Mapping(source = "clienteDTO.endereco", target = "endereco"),
			@Mapping(source = "clienteDTO.dataNascimento", target = "dataNascimento"),
			@Mapping(target = "veiculos", ignore = true) })
	Cliente toDomain(ClienteDTO clienteDTO);

	@Mappings({ 
			@Mapping(source = "cliente.id", target = "id"), 
			@Mapping(source = "cliente.nome", target = "nome"),
			@Mapping(source = "cliente.cpf", target = "cpf"),
			@Mapping(source = "cliente.telefone", target = "telefone"),
			@Mapping(source = "cliente.whatsapp", target = "whatsapp"),
			@Mapping(source = "cliente.email", target = "email"),
			@Mapping(source = "cliente.endereco", target = "endereco"),
			@Mapping(source = "cliente.dataNascimento", target = "dataNascimento") })
	ClienteDTO toDto(Cliente cliente);

	default Cliente fromJpaToDomain(JpaCliente jpaCliente) {
		if (jpaCliente == null) {
			return null;
		}

		LoggerFactory.getLogger(getClass()).info(" > jpaCliente: {}", jpaCliente);

		Cpf cpf = new Cpf(jpaCliente.getCpf().getCpf());

		Telefone telefone = new Telefone(jpaCliente.getTelefone().getNumero(), jpaCliente.getTelefone().getTipo());

		Telefone whatsapp = new Telefone(jpaCliente.getTelefone().getNumero(), jpaCliente.getTelefone().getTipo());

		Email email = new Email(jpaCliente.getEmail().getEmail());

		List<Veiculo> veiculos = new ArrayList<>();
		if (jpaCliente.getVeiculos() == null) {

		} else {
			veiculos = jpaCliente.getVeiculos().stream().map(veiculo -> new Veiculo(veiculo.getPlaca(),
					veiculo.getModelo(), veiculo.getMarca())).toList();
		}

		Endereco endereco = new Endereco(jpaCliente.getEndereco().getTipo(), jpaCliente.getEndereco().getLogradouro(), jpaCliente.getEndereco().getNumero(),
				jpaCliente.getEndereco().getComplemento(), jpaCliente.getEndereco().getBairro(),
				jpaCliente.getEndereco().getCidade(), jpaCliente.getEndereco().getEstado(),
				jpaCliente.getEndereco().getCep());

		Cliente cliente = new Cliente(jpaCliente.getId(), jpaCliente.getNome(), cpf, telefone, whatsapp, email,
				endereco, jpaCliente.getDataNascimento());
		cliente.setVeiculos(veiculos);

		return cliente;
	}

	default ClienteDTO fromJpaToDto(JpaCliente jpaCliente) {
		if (jpaCliente == null) {
			return null;
		}

		Cpf cpf = new Cpf(jpaCliente.getCpf().getCpf());

		Telefone telefone = new Telefone(jpaCliente.getTelefone().getNumero(), jpaCliente.getTelefone().getTipo());

		Telefone whatsapp = new Telefone(jpaCliente.getTelefone().getNumero(), jpaCliente.getTelefone().getTipo());

		Email email = new Email(jpaCliente.getEmail().getEmail());

		Endereco endereco = new Endereco(jpaCliente.getEndereco().getTipo(), jpaCliente.getEndereco().getLogradouro(), jpaCliente.getEndereco().getNumero(),
				jpaCliente.getEndereco().getComplemento(), jpaCliente.getEndereco().getBairro(),
				jpaCliente.getEndereco().getCidade(), jpaCliente.getEndereco().getEstado(),
				jpaCliente.getEndereco().getCep());

		List<VeiculoDTO> veiculos = new ArrayList<>();
		if (jpaCliente.getVeiculos() == null) {

		} else
			veiculos = jpaCliente.getVeiculos().stream()
					.map(veiculo -> new VeiculoDTO(veiculo.getPlaca(), veiculo.getModelo(),
							veiculo.getMarca(), veiculo.getValidado()))
					.toList();

		return new ClienteDTO(jpaCliente.getId(), jpaCliente.getNome(), cpf, telefone, whatsapp, email, endereco,
				jpaCliente.getDataNascimento(), veiculos);
	}

}
