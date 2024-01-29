package com.prueba.consulta.domain;

import static com.prueba.consulta.domain.CitaTestSamples.*;
import static com.prueba.consulta.domain.ConsultorioTestSamples.*;
import static com.prueba.consulta.domain.DoctorTestSamples.*;
import static com.prueba.consulta.domain.PacienteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prueba.consulta.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CitaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cita.class);
        Cita cita1 = getCitaSample1();
        Cita cita2 = new Cita();
        assertThat(cita1).isNotEqualTo(cita2);

        cita2.setId(cita1.getId());
        assertThat(cita1).isEqualTo(cita2);

        cita2 = getCitaSample2();
        assertThat(cita1).isNotEqualTo(cita2);
    }

    @Test
    void doctorTest() throws Exception {
        Cita cita = getCitaRandomSampleGenerator();
        Doctor doctorBack = getDoctorRandomSampleGenerator();

        cita.setDoctor(doctorBack);
        assertThat(cita.getDoctor()).isEqualTo(doctorBack);

        cita.doctor(null);
        assertThat(cita.getDoctor()).isNull();
    }

    @Test
    void consultorioTest() throws Exception {
        Cita cita = getCitaRandomSampleGenerator();
        Consultorio consultorioBack = getConsultorioRandomSampleGenerator();

        cita.setConsultorio(consultorioBack);
        assertThat(cita.getConsultorio()).isEqualTo(consultorioBack);

        cita.consultorio(null);
        assertThat(cita.getConsultorio()).isNull();
    }

    @Test
    void pacienteTest() throws Exception {
        Cita cita = getCitaRandomSampleGenerator();
        Paciente pacienteBack = getPacienteRandomSampleGenerator();

        cita.setPaciente(pacienteBack);
        assertThat(cita.getPaciente()).isEqualTo(pacienteBack);

        cita.paciente(null);
        assertThat(cita.getPaciente()).isNull();
    }
}
