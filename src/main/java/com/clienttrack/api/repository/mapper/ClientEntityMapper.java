package com.clienttrack.api.repository.mapper;

import com.clienttrack.api.controller.dto.ClientData;
import com.clienttrack.api.core.Client;
import com.clienttrack.api.repository.entity.ClientEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper {

    Client toCore(ClientEntity clientEntity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ClientData clientData, @MappingTarget ClientEntity clientEntity);
}
