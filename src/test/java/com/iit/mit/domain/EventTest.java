package com.iit.mit.domain;

import static com.iit.mit.domain.ConferenceTestSamples.*;
import static com.iit.mit.domain.EventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iit.mit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Event.class);
        Event event1 = getEventSample1();
        Event event2 = new Event();
        assertThat(event1).isNotEqualTo(event2);

        event2.setId(event1.getId());
        assertThat(event1).isEqualTo(event2);

        event2 = getEventSample2();
        assertThat(event1).isNotEqualTo(event2);
    }

    @Test
    void conferenceTest() {
        Event event = getEventRandomSampleGenerator();
        Conference conferenceBack = getConferenceRandomSampleGenerator();

        event.setConference(conferenceBack);
        assertThat(event.getConference()).isEqualTo(conferenceBack);

        event.conference(null);
        assertThat(event.getConference()).isNull();
    }
}
