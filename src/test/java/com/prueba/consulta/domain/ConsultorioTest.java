package com.prueba.consulta.domain;

import static com.prueba.consulta.domain.CitaTestSamples.*;
import static com.prueba.consulta.domain.ConsultorioTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prueba.consulta.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConsultorioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consultorio.class);
        Consultorio consultorio1 = getConsultorioSample1();
        Consultorio consultorio2 = new Consultorio();
        assertThat(consultorio1).isNotEqualTo(consultorio2);

        consultorio2.setId(consultorio1.getId());
        assertThat(consultorio1).isEqualTo(consultorio2);

        consultorio2 = getConsultorioSample2();
        assertThat(consultorio1).isNotEqualTo(consultorio2);
    }

    @Test
    void citaTest() throws Exception {
        Consultorio consultorio = getConsultorioRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        consultorio.addCita(citaBack);
        assertThat(consultorio.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getConsultorio()).isEqualTo(consultorio);

        consultorio.removeCita(citaBack);
        assertThat(consultorio.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getConsultorio()).isNull();

        consultorio.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(consultorio.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getConsultorio()).isEqualTo(consultorio);

        consultorio.setCitas(new HashSet<>());
        assertThat(consultorio.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getConsultorio()).isNull();
    }
}
