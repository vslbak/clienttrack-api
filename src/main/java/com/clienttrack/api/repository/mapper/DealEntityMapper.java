package com.clienttrack.api.repository.mapper;

import com.clienttrack.api.controller.dto.DealData;
import com.clienttrack.api.controller.dto.DealPatchData;
import com.clienttrack.api.core.Deal;
import com.clienttrack.api.repository.entity.DealEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface DealEntityMapper {

    Deal toCore(DealEntity clientEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(DealData dealData, @MappingTarget DealEntity dealEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "title", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(DealPatchData dealData, @MappingTarget DealEntity dealEntity);
}
