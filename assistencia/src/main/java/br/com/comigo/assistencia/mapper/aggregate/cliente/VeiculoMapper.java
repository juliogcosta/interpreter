package br.com.comigo.assistencia.mapper.aggregate.cliente;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.comigo.assistencia.adapter.aggregate.cliente.dto.VeiculoDTO;
import br.com.comigo.assistencia.adapter.aggregate.cliente.outbound.JpaVeiculo;
import br.com.comigo.assistencia.domain.aggregate.cliente.Veiculo;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {

	@Mappings({ 
			@Mapping(source = "veiculoDTO.marca", target = "marca"),
			@Mapping(source = "veiculoDTO.modelo", target = "modelo"),
			@Mapping(source = "veiculoDTO.placa", target = "placa"),
			@Mapping(source = "veiculoDTO.validado", target = "validado") })
	Veiculo toDomain(VeiculoDTO veiculoDTO);

	@Mappings({ 
			@Mapping(source = "veiculo.marca", target = "marca"),
			@Mapping(source = "veiculo.modelo", target = "modelo"), 
			@Mapping(source = "veiculo.placa", target = "placa"), 
			@Mapping(source = "veiculo.validado", target = "validado") })
	VeiculoDTO toDto(Veiculo veiculo);

	@Mappings({
			@Mapping(source = "jpaVeiculo.marca", target = "marca"),
			@Mapping(source = "jpaVeiculo.modelo", target = "modelo"),
			@Mapping(source = "jpaVeiculo.placa", target = "placa"),
			@Mapping(source = "jpaVeiculo.validado", target = "validado") })
	Veiculo fromJpaToDomain(JpaVeiculo jpaVeiculo);

	@Mappings({ 
			@Mapping(source = "jpaVeiculo.marca", target = "marca"),
			@Mapping(source = "jpaVeiculo.modelo", target = "modelo"),
			@Mapping(source = "jpaVeiculo.placa", target = "placa"),
			@Mapping(source = "jpaVeiculo.validado", target = "validado") })
	VeiculoDTO fromJpaToDto(JpaVeiculo jpaVeiculo);
}
