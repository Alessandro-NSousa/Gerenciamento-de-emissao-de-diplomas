package com.ufpa.diploma.emissao.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ufpa.diploma.emissao.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TurmaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Turma.class);
        Turma turma1 = new Turma();
        turma1.setId(1L);
        Turma turma2 = new Turma();
        turma2.setId(turma1.getId());
        assertThat(turma1).isEqualTo(turma2);
        turma2.setId(2L);
        assertThat(turma1).isNotEqualTo(turma2);
        turma1.setId(null);
        assertThat(turma1).isNotEqualTo(turma2);
    }
}
