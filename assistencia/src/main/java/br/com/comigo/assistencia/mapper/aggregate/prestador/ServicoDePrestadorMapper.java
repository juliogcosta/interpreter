package br.com.comigo.assistencia.mapper.aggregate.prestador;

import java.util.List;

import org.mapstruct.Mapper;

import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.PrestadorResponse;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorDTO.ItemDeServicoDePrestadorDTO;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorResponse;
import br.com.comigo.assistencia.adapter.aggregate.prestador.dto.ServicoDePrestadorResponse.ItemDeServicoDePrestadorResponse;
import br.com.comigo.assistencia.adapter.aggregate.prestador.outbound.JpaServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ItemDeServicoDePrestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.Prestador;
import br.com.comigo.assistencia.domain.aggregate.prestador.ServicoDePrestador;
import br.com.comigo.common.model.records.Cnpj;
import br.com.comigo.common.model.records.Email;
import br.com.comigo.common.model.records.Endereco;
import br.com.comigo.common.model.records.Telefone;

@Mapper(componentModel = "spring")
public interface ServicoDePrestadorMapper {

	default ServicoDePrestador toDomain(ServicoDePrestadorDTO servicoDePrestadorDTO) {
		List<ItemDeServicoDePrestador> itemDeServicoDePrestadors = null;
		if (servicoDePrestadorDTO.itemDeServicoDePrestadors() == null) {
		
		} else {
			itemDeServicoDePrestadors = servicoDePrestadorDTO.itemDeServicoDePrestadors().stream()
					.map(itemDeServicoDePrestadorDTO -> {
						return new ItemDeServicoDePrestador(
								itemDeServicoDePrestadorDTO.nome(), 
								itemDeServicoDePrestadorDTO.unidadeDeMedida(), 
								itemDeServicoDePrestadorDTO.precoUnitario());
					}).toList();
		}
		
		ServicoDePrestador servicoDePrestador = new ServicoDePrestador(
				servicoDePrestadorDTO.id(),
				null,
				servicoDePrestadorDTO.status(),
				null, 
				itemDeServicoDePrestadors);
		return servicoDePrestador;
	}

	default ServicoDePrestadorDTO toDto(ServicoDePrestador servicoDePrestador) {
		List<ItemDeServicoDePrestadorDTO> itemDeServicoDePrestadorDTOs = null;
		if (servicoDePrestador.getItemDeServicoDePrestadors() == null) {
		
		} else {
			itemDeServicoDePrestadorDTOs = servicoDePrestador.getItemDeServicoDePrestadors().stream()
					.map(itemDeServicoDePrestador -> {
						return new ItemDeServicoDePrestadorDTO(
								itemDeServicoDePrestador.getNome(), 
								itemDeServicoDePrestador.getUnidadeDeMedida(), 
								itemDeServicoDePrestador.getPrecoUnitario());
					}).toList();
		}
		
		ServicoDePrestadorDTO servicoDePrestadorDTO = new ServicoDePrestadorDTO(
				servicoDePrestador.getId(),
				servicoDePrestador.getStatus(),
				itemDeServicoDePrestadorDTOs);
		return servicoDePrestadorDTO;
	}

	default ServicoDePrestadorResponse toResponse(ServicoDePrestador servicoDePrestador, boolean full) {
		List<ItemDeServicoDePrestadorResponse> itemDeServicoDePrestadorResponses = null;
		if (servicoDePrestador.getItemDeServicoDePrestadors() == null) {
		
		} else {
			itemDeServicoDePrestadorResponses = servicoDePrestador.getItemDeServicoDePrestadors().stream()
					.map(itemDeServicoDePrestador -> {
						return new ServicoDePrestadorResponse.ItemDeServicoDePrestadorResponse(
								itemDeServicoDePrestador.getNome(), 
								itemDeServicoDePrestador.getUnidadeDeMedida(), 
								itemDeServicoDePrestador.getPrecoUnitario());
					}).toList();
		}
		
		PrestadorResponse prestadorResponse = null;
		if (full) {
			prestadorResponse = new PrestadorResponse(
					servicoDePrestador.getPrestador().getId(), 
					servicoDePrestador.getPrestador().getNome(), 
					servicoDePrestador.getPrestador().getCnpj(), 
					servicoDePrestador.getPrestador().getTelefone(), 
					servicoDePrestador.getPrestador().getWhatsapp(), 
					servicoDePrestador.getPrestador().getEmail(), 
					servicoDePrestador.getPrestador().getEndereco(), 
					servicoDePrestador.getPrestador().getStatus(), 
					servicoDePrestador.getPrestador().getTemCertificacaoIso());
		}
		
		ServicoDePrestadorResponse servicoDePrestadorResponse = new ServicoDePrestadorResponse(
				servicoDePrestador.getId(),
				servicoDePrestador.getNome(),
				servicoDePrestador.getStatus(),
				prestadorResponse,
				itemDeServicoDePrestadorResponses);
		return servicoDePrestadorResponse;
	}

	default ServicoDePrestador fromJpaToDomain(JpaServicoDePrestador jpaServicoDePrestador) {
		List<ItemDeServicoDePrestador> itemDeServicoDePrestadors = null;
		if (jpaServicoDePrestador.getItemDeServicoDePrestadors() == null) {
		
		} else {
			itemDeServicoDePrestadors = jpaServicoDePrestador.getItemDeServicoDePrestadors().stream()
					.map(jpaItemDeServicoDePrestador -> {
						return new ItemDeServicoDePrestador(
								jpaItemDeServicoDePrestador.getNome(),
								jpaItemDeServicoDePrestador.getUnidadeDeMedida(),
								jpaItemDeServicoDePrestador.getPrecoUnitario());
					}).toList();
		}
		
		Prestador prestador = null;
		if (jpaServicoDePrestador.getPrestador() == null) {
			
		} else {
			Cnpj cnpj = new Cnpj(jpaServicoDePrestador.getPrestador().getCnpj().getCnpj());
			
			Telefone telefone = new Telefone(
					jpaServicoDePrestador.getPrestador().getTelefone().getNumero(), 
					jpaServicoDePrestador.getPrestador().getTelefone().getTipo());

			Telefone whatsapp = new Telefone(
					jpaServicoDePrestador.getPrestador().getTelefone().getNumero(), 
					jpaServicoDePrestador.getPrestador().getTelefone().getTipo());
			
			Email email = new Email(jpaServicoDePrestador.getPrestador().getEmail().getEmail());
			
			Endereco endereco = new Endereco(
					jpaServicoDePrestador.getPrestador().getEndereco().getTipo(),
					jpaServicoDePrestador.getPrestador().getEndereco().getLogradouro(), 
					jpaServicoDePrestador.getPrestador().getEndereco().getNumero(), 
					jpaServicoDePrestador.getPrestador().getEndereco().getComplemento(), 
					jpaServicoDePrestador.getPrestador().getEndereco().getBairro(), 
					jpaServicoDePrestador.getPrestador().getEndereco().getCidade(), 
					jpaServicoDePrestador.getPrestador().getEndereco().getEstado(), 
					jpaServicoDePrestador.getPrestador().getEndereco().getCep());
			
			prestador = new Prestador(
					jpaServicoDePrestador.getPrestador().getId(), 
					jpaServicoDePrestador.getPrestador().getNome(), 
					jpaServicoDePrestador.getPrestador().getStatus(),
					cnpj,
					telefone, 
					whatsapp, 
					email, 
					endereco,
					jpaServicoDePrestador.getPrestador().getTemCertificacaoIso());
		}
		
		ServicoDePrestador servicoDePrestador = new ServicoDePrestador(
				jpaServicoDePrestador.getId(),
				null,
				jpaServicoDePrestador.getStatus(),
				prestador,
				itemDeServicoDePrestadors);
		return servicoDePrestador;
	}

	default ServicoDePrestadorDTO fromJpaToDto(JpaServicoDePrestador jpaServicoDePrestador) {
		List<ItemDeServicoDePrestadorDTO> itemDeServicoDePrestadorDTOs = null;
		if (jpaServicoDePrestador.getItemDeServicoDePrestadors() == null) {
		
		} else {
			itemDeServicoDePrestadorDTOs = jpaServicoDePrestador.getItemDeServicoDePrestadors().stream()
					.map(jpaItemDeServicoDePrestador -> {
						return new ItemDeServicoDePrestadorDTO(
								jpaItemDeServicoDePrestador.getNome(),
								jpaItemDeServicoDePrestador.getUnidadeDeMedida(),
								jpaItemDeServicoDePrestador.getPrecoUnitario());
					}).toList();
		}
		ServicoDePrestadorDTO servicoDePrestador = new ServicoDePrestadorDTO(
				jpaServicoDePrestador.getId(),
				jpaServicoDePrestador.getStatus(),
				itemDeServicoDePrestadorDTOs);
		return servicoDePrestador;
	}

	default ServicoDePrestadorResponse fromDomainToResponse(ServicoDePrestador servicoDePrestador, boolean full) {
		List<ItemDeServicoDePrestadorResponse> itemDeServicoDePrestadorResponses = null;
		if (servicoDePrestador.getItemDeServicoDePrestadors() == null) {
		
		} else {
			itemDeServicoDePrestadorResponses = servicoDePrestador.getItemDeServicoDePrestadors().stream()
					.map(jpaItemDeServicoDePrestador -> {
						return new ItemDeServicoDePrestadorResponse(
								jpaItemDeServicoDePrestador.getNome(),
								jpaItemDeServicoDePrestador.getUnidadeDeMedida(),
								jpaItemDeServicoDePrestador.getPrecoUnitario());
					}).toList();
		}

		PrestadorResponse prestadorResponse = null;
		if (full) {
			prestadorResponse = new PrestadorResponse(
					servicoDePrestador.getPrestador().getId(), 
					servicoDePrestador.getPrestador().getNome(), 
					servicoDePrestador.getPrestador().getCnpj(), 
					servicoDePrestador.getPrestador().getTelefone(), 
					servicoDePrestador.getPrestador().getWhatsapp(), 
					servicoDePrestador.getPrestador().getEmail(), 
					servicoDePrestador.getPrestador().getEndereco(), 
					servicoDePrestador.getPrestador().getStatus(), 
					servicoDePrestador.getPrestador().getTemCertificacaoIso());
		}
		
		ServicoDePrestadorResponse servicoDePrestadorResponse = new ServicoDePrestadorResponse(
				servicoDePrestador.getId(),
				servicoDePrestador.getNome(),
				servicoDePrestador.getStatus(),
				prestadorResponse,
				itemDeServicoDePrestadorResponses);
		return servicoDePrestadorResponse;
	}
}
