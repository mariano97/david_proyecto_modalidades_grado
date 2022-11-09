package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Observaciones;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ObservacionesDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProyectoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Observaciones} and its DTO {@link ObservacionesDTO}.
 */
@Mapper(componentModel = "spring")
public interface ObservacionesMapper extends EntityMapper<ObservacionesDTO, Observaciones> {
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoNombre")
    ObservacionesDTO toDto(Observaciones s);

    @Named("proyectoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProyectoDTO toDtoProyectoNombre(Proyecto proyecto);
}
