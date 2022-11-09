package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaContenidoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaContenidoDTO.class);
        TablaContenidoDTO tablaContenidoDTO1 = new TablaContenidoDTO();
        tablaContenidoDTO1.setId(1L);
        TablaContenidoDTO tablaContenidoDTO2 = new TablaContenidoDTO();
        assertThat(tablaContenidoDTO1).isNotEqualTo(tablaContenidoDTO2);
        tablaContenidoDTO2.setId(tablaContenidoDTO1.getId());
        assertThat(tablaContenidoDTO1).isEqualTo(tablaContenidoDTO2);
        tablaContenidoDTO2.setId(2L);
        assertThat(tablaContenidoDTO1).isNotEqualTo(tablaContenidoDTO2);
        tablaContenidoDTO1.setId(null);
        assertThat(tablaContenidoDTO1).isNotEqualTo(tablaContenidoDTO2);
    }
}
