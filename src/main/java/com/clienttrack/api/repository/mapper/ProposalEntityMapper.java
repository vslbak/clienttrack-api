package com.clienttrack.api.repository.mapper;

import com.clienttrack.api.controller.dto.ProposalData;
import com.clienttrack.api.core.Proposal;
import com.clienttrack.api.repository.entity.ProposalEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProposalEntityMapper {

    Proposal toCore(ProposalEntity proposalEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deal", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ProposalData proposalData, @MappingTarget ProposalEntity proposalEntity);
}
