package com.prueba.consulta.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PacienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Paciente getPacienteSample1() {
        return new Paciente().id(1L).nombre("nombre1");
    }

    public static Paciente getPacienteSample2() {
        return new Paciente().id(2L).nombre("nombre2");
    }

    public static Paciente getPacienteRandomSampleGenerator() {
        return new Paciente().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString());
    }
}
