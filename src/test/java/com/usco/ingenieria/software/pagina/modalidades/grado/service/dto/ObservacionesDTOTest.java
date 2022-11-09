package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ObservacionesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ObservacionesDTO.class);
        ObservacionesDTO observacionesDTO1 = new ObservacionesDTO();
        observacionesDTO1.setId(1L);
        ObservacionesDTO observacionesDTO2 = new ObservacionesDTO();
        assertThat(observacionesDTO1).isNotEqualTo(observacionesDTO2);
        observacionesDTO2.setId(observacionesDTO1.getId());
        assertThat(observacionesDTO1).isEqualTo(observacionesDTO2);
        observacionesDTO2.setId(2L);
        assertThat(observacionesDTO1).isNotEqualTo(observacionesDTO2);
        observacionesDTO1.setId(null);
        assertThat(observacionesDTO1).isNotEqualTo(observacionesDTO2);
    }
}
