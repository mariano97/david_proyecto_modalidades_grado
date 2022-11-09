package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObservacionesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Observaciones.class);
        Observaciones observaciones1 = new Observaciones();
        observaciones1.setId(1L);
        Observaciones observaciones2 = new Observaciones();
        observaciones2.setId(observaciones1.getId());
        assertThat(observaciones1).isEqualTo(observaciones2);
        observaciones2.setId(2L);
        assertThat(observaciones1).isNotEqualTo(observaciones2);
        observaciones1.setId(null);
        assertThat(observaciones1).isNotEqualTo(observaciones2);
    }
}
