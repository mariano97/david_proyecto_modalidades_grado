package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaContenidoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaContenido.class);
        TablaContenido tablaContenido1 = new TablaContenido();
        tablaContenido1.setId(1L);
        TablaContenido tablaContenido2 = new TablaContenido();
        tablaContenido2.setId(tablaContenido1.getId());
        assertThat(tablaContenido1).isEqualTo(tablaContenido2);
        tablaContenido2.setId(2L);
        assertThat(tablaContenido1).isNotEqualTo(tablaContenido2);
        tablaContenido1.setId(null);
        assertThat(tablaContenido1).isNotEqualTo(tablaContenido2);
    }
}
