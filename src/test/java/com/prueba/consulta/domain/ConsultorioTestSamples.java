package com.prueba.consulta.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConsultorioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Consultorio getConsultorioSample1() {
        return new Consultorio().id(1L).numero("numero1").piso("piso1");
    }

    public static Consultorio getConsultorioSample2() {
        return new Consultorio().id(2L).numero("numero2").piso("piso2");
    }

    public static Consultorio getConsultorioRandomSampleGenerator() {
        return new Consultorio().id(longCount.incrementAndGet()).numero(UUID.randomUUID().toString()).piso(UUID.randomUUID().toString());
    }
}
