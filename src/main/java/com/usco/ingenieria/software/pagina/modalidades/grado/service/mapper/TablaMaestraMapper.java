package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaMaestraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TablaMaestra} and its DTO {@link TablaMaestraDTO}.
 */
@Mapper(componentModel = "spring")
public interface TablaMaestraMapper extends EntityMapper<TablaMaestraDTO, TablaMaestra> {}
