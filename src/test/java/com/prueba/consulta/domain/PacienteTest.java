package com.prueba.consulta.domain;

import static com.prueba.consulta.domain.CitaTestSamples.*;
import static com.prueba.consulta.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prueba.consulta.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paciente.class);
        Paciente paciente1 = getPacienteSample1();
        Paciente paciente2 = new Paciente();
        assertThat(paciente1).isNotEqualTo(paciente2);

        paciente2.setId(paciente1.getId());
        assertThat(paciente1).isEqualTo(paciente2);

        paciente2 = getPacienteSample2();
        assertThat(paciente1).isNotEqualTo(paciente2);
    }

    @Test
    void citaTest() throws Exception {
        Paciente paciente = getPacienteRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        paciente.addCita(citaBack);
        assertThat(paciente.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getPaciente()).isEqualTo(paciente);

        paciente.removeCita(citaBack);
        assertThat(paciente.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getPaciente()).isNull();

        paciente.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(paciente.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getPaciente()).isEqualTo(paciente);

        paciente.setCitas(new HashSet<>());
        assertThat(paciente.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getPaciente()).isNull();
    }
}
