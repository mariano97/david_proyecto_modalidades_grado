package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProyectoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proyecto.class);
        Proyecto proyecto1 = new Proyecto();
        proyecto1.setId(1L);
        Proyecto proyecto2 = new Proyecto();
        proyecto2.setId(proyecto1.getId());
        assertThat(proyecto1).isEqualTo(proyecto2);
        proyecto2.setId(2L);
        assertThat(proyecto1).isNotEqualTo(proyecto2);
        proyecto1.setId(null);
        assertThat(proyecto1).isNotEqualTo(proyecto2);
    }
}
