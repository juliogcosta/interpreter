package br.com.comigo.id.mapper.aggregate.papel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import br.com.comigo.id.adapter.aggregate.papel.dto.PapelDTO;
import br.com.comigo.id.adapter.aggregate.papel.outbound.JpaPapel;
import br.com.comigo.id.domain.aggregate.papel.Papel;

@Mapper(componentModel = "spring")
public interface PapelMapper {
        @Mappings({ @Mapping(source = "papelDTO.id", target = "id"),
                        @Mapping(source = "papelDTO.nome", target = "nome"),
                        @Mapping(source = "papelDTO.status", target = "status") })
        Papel toDomain(PapelDTO papelDTO);

        @Mappings({ @Mapping(source = "papel.id", target = "id"), @Mapping(source = "papel.nome", target = "nome"),
                        @Mapping(source = "papel.status", target = "status") })
        PapelDTO toDto(Papel papel);

        @Mappings({ @Mapping(source = "jpaPapel.id", target = "id"),
                        @Mapping(source = "jpaPapel.nome", target = "nome"),
                        @Mapping(source = "jpaPapel.status", target = "status"), })
        Papel fromJpaToDomain(JpaPapel jpaPapel);

        @Mappings({ @Mapping(source = "jpaPapel.id", target = "id"),
                        @Mapping(source = "jpaPapel.nome", target = "nome"),
                        @Mapping(source = "jpaPapel.status", target = "status"), })
        PapelDTO fromJpaToDto(JpaPapel jpaPapel);
}
