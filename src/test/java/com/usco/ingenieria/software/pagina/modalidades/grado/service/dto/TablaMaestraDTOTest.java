package com.usco.ingenieria.software.pagina.modalidades.grado.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.usco.ingenieria.software.pagina.modalidades.grado.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TablaMaestraDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TablaMaestraDTO.class);
        TablaMaestraDTO tablaMaestraDTO1 = new TablaMaestraDTO();
        tablaMaestraDTO1.setId(1L);
        TablaMaestraDTO tablaMaestraDTO2 = new TablaMaestraDTO();
        assertThat(tablaMaestraDTO1).isNotEqualTo(tablaMaestraDTO2);
        tablaMaestraDTO2.setId(tablaMaestraDTO1.getId());
        assertThat(tablaMaestraDTO1).isEqualTo(tablaMaestraDTO2);
        tablaMaestraDTO2.setId(2L);
        assertThat(tablaMaestraDTO1).isNotEqualTo(tablaMaestraDTO2);
        tablaMaestraDTO1.setId(null);
        assertThat(tablaMaestraDTO1).isNotEqualTo(tablaMaestraDTO2);
    }
}
