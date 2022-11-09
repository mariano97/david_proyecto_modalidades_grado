package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaMaestraTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaMaestra.class);
        TablaMaestra tablaMaestra1 = new TablaMaestra();
        tablaMaestra1.setId(1L);
        TablaMaestra tablaMaestra2 = new TablaMaestra();
        tablaMaestra2.setId(tablaMaestra1.getId());
        assertThat(tablaMaestra1).isEqualTo(tablaMaestra2);
        tablaMaestra2.setId(2L);
        assertThat(tablaMaestra1).isNotEqualTo(tablaMaestra2);
        tablaMaestra1.setId(null);
        assertThat(tablaMaestra1).isNotEqualTo(tablaMaestra2);
    }
}
