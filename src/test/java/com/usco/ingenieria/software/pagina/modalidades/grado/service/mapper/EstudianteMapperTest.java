package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EstudianteMapperTest {

    private EstudianteMapper estudianteMapper;

    @BeforeEach
    public void setUp() {
        estudianteMapper = new EstudianteMapperImpl();
    }
}
