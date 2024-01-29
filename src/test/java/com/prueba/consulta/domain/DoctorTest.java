package com.prueba.consulta.domain;

import static com.prueba.consulta.domain.CitaTestSamples.*;
import static com.prueba.consulta.domain.DoctorTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.prueba.consulta.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DoctorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Doctor.class);
        Doctor doctor1 = getDoctorSample1();
        Doctor doctor2 = new Doctor();
        assertThat(doctor1).isNotEqualTo(doctor2);

        doctor2.setId(doctor1.getId());
        assertThat(doctor1).isEqualTo(doctor2);

        doctor2 = getDoctorSample2();
        assertThat(doctor1).isNotEqualTo(doctor2);
    }

    @Test
    void citaTest() throws Exception {
        Doctor doctor = getDoctorRandomSampleGenerator();
        Cita citaBack = getCitaRandomSampleGenerator();

        doctor.addCita(citaBack);
        assertThat(doctor.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getDoctor()).isEqualTo(doctor);

        doctor.removeCita(citaBack);
        assertThat(doctor.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getDoctor()).isNull();

        doctor.citas(new HashSet<>(Set.of(citaBack)));
        assertThat(doctor.getCitas()).containsOnly(citaBack);
        assertThat(citaBack.getDoctor()).isEqualTo(doctor);

        doctor.setCitas(new HashSet<>());
        assertThat(doctor.getCitas()).doesNotContain(citaBack);
        assertThat(citaBack.getDoctor()).isNull();
    }
}
