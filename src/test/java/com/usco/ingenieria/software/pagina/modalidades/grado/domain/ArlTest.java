package com.usco.ingenieria.software.pagina.modalidades.grado.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Arl.class);
        Arl arl1 = new Arl();
        arl1.setId(1L);
        Arl arl2 = new Arl();
        arl2.setId(arl1.getId());
        assertThat(arl1).isEqualTo(arl2);
        arl2.setId(2L);
        assertThat(arl1).isNotEqualTo(arl2);
        arl1.setId(null);
        assertThat(arl1).isNotEqualTo(arl2);
    }
}
