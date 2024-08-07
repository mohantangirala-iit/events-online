package com.iit.mit.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Conference getConferenceSample1() {
        return new Conference().id(1L).conferenceName("conferenceName1");
    }

    public static Conference getConferenceSample2() {
        return new Conference().id(2L).conferenceName("conferenceName2");
    }

    public static Conference getConferenceRandomSampleGenerator() {
        return new Conference().id(longCount.incrementAndGet()).conferenceName(UUID.randomUUID().toString());
    }
}
