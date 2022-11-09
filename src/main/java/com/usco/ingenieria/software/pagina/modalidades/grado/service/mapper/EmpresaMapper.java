package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import com.usco.ingenieria.software.pagina.modalidades.grado.domain.Empresa;
import com.usco.ingenieria.software.pagina.modalidades.grado.service.dto.EmpresaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empresa} and its DTO {@link EmpresaDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpresaMapper extends EntityMapper<EmpresaDTO, Empresa> {}
