package com.iit.mit.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PersonTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PersonType getPersonTypeSample1() {
        return new PersonType().id(1L).jobTitle(1L);
    }

    public static PersonType getPersonTypeSample2() {
        return new PersonType().id(2L).jobTitle(2L);
    }

    public static PersonType getPersonTypeRandomSampleGenerator() {
        return new PersonType().id(longCount.incrementAndGet()).jobTitle(longCount.incrementAndGet());
    }
}
