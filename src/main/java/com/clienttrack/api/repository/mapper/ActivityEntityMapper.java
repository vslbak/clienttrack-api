package com.clienttrack.api.repository.mapper;

import com.clienttrack.api.controller.dto.ActivityData;
import com.clienttrack.api.core.Activity;
import com.clienttrack.api.repository.entity.ActivityEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ActivityEntityMapper {

    Activity toCore(ActivityEntity activityEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deal", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ActivityData activityData, @MappingTarget ActivityEntity activityEntity);
}
