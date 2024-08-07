package com.iit.mit.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Event getEventSample1() {
        return new Event().id(1L).title("title1").description("description1").audience("audience1").level("level1");
    }

    public static Event getEventSample2() {
        return new Event().id(2L).title("title2").description("description2").audience("audience2").level("level2");
    }

    public static Event getEventRandomSampleGenerator() {
        return new Event()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .audience(UUID.randomUUID().toString())
            .level(UUID.randomUUID().toString());
    }
}
