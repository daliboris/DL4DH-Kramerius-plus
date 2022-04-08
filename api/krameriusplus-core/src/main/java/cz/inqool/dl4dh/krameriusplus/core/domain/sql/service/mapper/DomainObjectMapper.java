package cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.mapper;

import cz.inqool.dl4dh.krameriusplus.core.domain.sql.dao.object.DomainObject;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DomainObjectCreateDto;
import cz.inqool.dl4dh.krameriusplus.core.domain.sql.service.dto.DomainObjectDto;

public interface DomainObjectMapper<ENTITY extends DomainObject, CDTO extends DomainObjectCreateDto, DTO extends DomainObjectDto> {

    ENTITY fromCreateDto(CDTO createDto);

    ENTITY fromDto(DTO dto);

    DTO toDto(ENTITY entity);

}
