package br.com.comigo.assistencia.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicoMapper {

    /*
     * @Mapping(source = "aggregateId", target = "id")
     * AtendimentoProjection toProjection(ServicoAggregate order);
     * 
     * @Mapping(source = "order.aggregateId", target = "orderId")
     * 
     * @Mapping(source = "event.eventType", target = "eventType")
     * 
     * @Mapping(source = "event.createdDate", target = "eventTimestamp")
     * 
     * @Mapping(source = "order.baseVersion", target = "version")
     * 
     * @Mapping(source = "order.riderId", target = "riderId")
     * 
     * @Mapping(source = "order.price", target = "price")
     * 
     * @Mapping(source = "order.route", target = "route")
     * 
     * @Mapping(source = "order.driverId", target = "driverId")
     * ServicoDTO toDto(Event event, ServicoAggregate order);
     */
}
