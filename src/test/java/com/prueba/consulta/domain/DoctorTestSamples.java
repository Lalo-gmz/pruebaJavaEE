package com.prueba.consulta.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DoctorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Doctor getDoctorSample1() {
        return new Doctor()
            .id(1L)
            .nombre("nombre1")
            .apellidoPaterno("apellidoPaterno1")
            .apellidoMaterno("apellidoMaterno1")
            .especialidad("especialidad1");
    }

    public static Doctor getDoctorSample2() {
        return new Doctor()
            .id(2L)
            .nombre("nombre2")
            .apellidoPaterno("apellidoPaterno2")
            .apellidoMaterno("apellidoMaterno2")
            .especialidad("especialidad2");
    }

    public static Doctor getDoctorRandomSampleGenerator() {
        return new Doctor()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .apellidoPaterno(UUID.randomUUID().toString())
            .apellidoMaterno(UUID.randomUUID().toString())
            .especialidad(UUID.randomUUID().toString());
    }
}
