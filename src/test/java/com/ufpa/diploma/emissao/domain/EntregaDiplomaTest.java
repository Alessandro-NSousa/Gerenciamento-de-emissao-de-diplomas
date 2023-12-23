package com.ufpa.diploma.emissao.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ufpa.diploma.emissao.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntregaDiplomaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntregaDiploma.class);
        EntregaDiploma entregaDiploma1 = new EntregaDiploma();
        entregaDiploma1.setId(1L);
        EntregaDiploma entregaDiploma2 = new EntregaDiploma();
        entregaDiploma2.setId(entregaDiploma1.getId());
        assertThat(entregaDiploma1).isEqualTo(entregaDiploma2);
        entregaDiploma2.setId(2L);
        assertThat(entregaDiploma1).isNotEqualTo(entregaDiploma2);
        entregaDiploma1.setId(null);
        assertThat(entregaDiploma1).isNotEqualTo(entregaDiploma2);
    }
}
