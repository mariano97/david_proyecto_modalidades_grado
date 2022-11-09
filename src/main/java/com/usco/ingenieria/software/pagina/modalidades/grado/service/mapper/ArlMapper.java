package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ArlDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Arl} and its DTO {@link ArlDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArlMapper extends EntityMapper<ArlDTO, Arl> {}
