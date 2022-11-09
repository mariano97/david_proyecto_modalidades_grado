package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaContenido;
import com.usco.ingenieria.software.pagina.modalidades.grado.domain.TablaMaestra;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaContenidoDTO;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.TablaMaestraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TablaContenido} and its DTO {@link TablaContenidoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TablaContenidoMapper extends EntityMapper<TablaContenidoDTO, TablaContenido> {
    @Mapping(target = "tablaMaestra", source = "tablaMaestra", qualifiedByName = "tablaMaestraNombre")
    TablaContenidoDTO toDto(TablaContenido s);

    @Named("tablaMaestraNombre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nombre", source = "nombre")
    TablaMaestraDTO toDtoTablaMaestraNombre(TablaMaestra tablaMaestra);
}
