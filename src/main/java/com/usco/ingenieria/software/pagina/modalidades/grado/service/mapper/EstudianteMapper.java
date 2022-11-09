package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Estudiante;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EstudianteDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProyectoDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaContenidoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Estudiante} and its DTO {@link EstudianteDTO}.
 */
@Mapper(componentModel = "spring")
public interface EstudianteMapper extends EntityMapper<EstudianteDTO, Estudiante> {
    @Mapping(target = "tipoDocumento", source = "tipoDocumento", qualifiedByName = "tablaContenidoNombre")
    @Mapping(target = "proyecto", source = "proyecto", qualifiedByName = "proyectoNombre")
    EstudianteDTO toDto(Estudiante s);

    @Named("tablaContenidoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaContenidoDTO toDtoTablaContenidoNombre(TablaContenido tablaContenido);

    @Named("proyectoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ProyectoDTO toDtoProyectoNombre(Proyecto proyecto);
}
