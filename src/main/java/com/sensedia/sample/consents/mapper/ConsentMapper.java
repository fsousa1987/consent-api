package com.sensedia.sample.consents.mapper;

import com.sensedia.sample.consents.domain.Consent;
import com.sensedia.sample.consents.dto.ConsentRequestDTO;
import com.sensedia.sample.consents.dto.ConsentResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsentMapper {

    Consent toEntity(ConsentRequestDTO dto);
    ConsentResponseDTO toResponseDTO(Consent entity);

}
