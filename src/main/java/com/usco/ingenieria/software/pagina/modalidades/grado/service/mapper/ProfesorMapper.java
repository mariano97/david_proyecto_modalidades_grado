package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Profesor;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProfesorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profesor} and its DTO {@link ProfesorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfesorMapper extends EntityMapper<ProfesorDTO, Profesor> {}
