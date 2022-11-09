package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Arl;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Empresa;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Proyecto;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ArlDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EmpresaDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.ProyectoDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaContenidoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proyecto} and its DTO {@link ProyectoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProyectoMapper extends EntityMapper<ProyectoDTO, Proyecto> {
    @Mapping(target = "tipoModalidad", source = "tipoModalidad", qualifiedByName = "tablaContenidoNombre")
    @Mapping(target = "empresa", source = "empresa", qualifiedByName = "empresaNombre")
    @Mapping(target = "arl", source = "arl", qualifiedByName = "arlNombre")
    ProyectoDTO toDto(Proyecto s);

    @Named("tablaContenidoNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaContenidoDTO toDtoTablaContenidoNombre(TablaContenido tablaContenido);

    @Named("empresaNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    EmpresaDTO toDtoEmpresaNombre(Empresa empresa);

    @Named("arlNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    ArlDTO toDtoArlNombre(Arl arl);
}
