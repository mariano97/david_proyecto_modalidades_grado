package com.usco.ingenieria.software.pagina.modalidades.grado.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfesorMapperTest {

    private ProfesorMapper profesorMapper;

    @BeforeEach
    public void setUp() {
        profesorMapper = new ProfesorMapperImpl();
    }
}
