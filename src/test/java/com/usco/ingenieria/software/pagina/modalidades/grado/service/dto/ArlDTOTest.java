package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArlDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArlDTO.class);
        ArlDTO arlDTO1 = new ArlDTO();
        arlDTO1.setId(1L);
        ArlDTO arlDTO2 = new ArlDTO();
        assertThat(arlDTO1).isNotEqualTo(arlDTO2);
        arlDTO2.setId(arlDTO1.getId());
        assertThat(arlDTO1).isEqualTo(arlDTO2);
        arlDTO2.setId(2L);
        assertThat(arlDTO1).isNotEqualTo(arlDTO2);
        arlDTO1.setId(null);
        assertThat(arlDTO1).isNotEqualTo(arlDTO2);
    }
}
