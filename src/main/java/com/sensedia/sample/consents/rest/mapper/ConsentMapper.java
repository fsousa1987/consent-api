package com.sensedia.sample.consents.rest.mapper;

import com.sensedia.sample.consents.domain.model.Consent;
import com.sensedia.sample.consents.rest.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.rest.dto.ConsentResponseDTO;
import com.sensedia.sample.consents.rest.dto.ConsentUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConsentMapper {

    Consent toEntity(ConsentRequestDTO dto);
    ConsentResponseDTO toResponseDTO(Consent entity);
    void updateEntityFromDto(ConsentUpdateDTO dto, @MappingTarget Consent entity);

}
