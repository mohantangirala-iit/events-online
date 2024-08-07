package com.iit.mit.domain;

import static com.iit.mit.domain.ApplicationUserTestSamples.*;
import static com.iit.mit.domain.ConferenceTestSamples.*;
import static com.iit.mit.domain.EventTestSamples.*;
import static com.iit.mit.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iit.mit.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ConferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Conference.class);
        Conference conference1 = getConferenceSample1();
        Conference conference2 = new Conference();
        assertThat(conference1).isNotEqualTo(conference2);

        conference2.setId(conference1.getId());
        assertThat(conference1).isEqualTo(conference2);

        conference2 = getConferenceSample2();
        assertThat(conference1).isNotEqualTo(conference2);
    }

    @Test
    void locationTest() {
        Conference conference = getConferenceRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        conference.setLocation(locationBack);
        assertThat(conference.getLocation()).isEqualTo(locationBack);

        conference.location(null);
        assertThat(conference.getLocation()).isNull();
    }

    @Test
    void eventTest() {
        Conference conference = getConferenceRandomSampleGenerator();
        Event eventBack = getEventRandomSampleGenerator();

        conference.addEvent(eventBack);
        assertThat(conference.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getConference()).isEqualTo(conference);

        conference.removeEvent(eventBack);
        assertThat(conference.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getConference()).isNull();

        conference.events(new HashSet<>(Set.of(eventBack)));
        assertThat(conference.getEvents()).containsOnly(eventBack);
        assertThat(eventBack.getConference()).isEqualTo(conference);

        conference.setEvents(new HashSet<>());
        assertThat(conference.getEvents()).doesNotContain(eventBack);
        assertThat(eventBack.getConference()).isNull();
    }

    @Test
    void applicationUserTest() {
        Conference conference = getConferenceRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        conference.setApplicationUser(applicationUserBack);
        assertThat(conference.getApplicationUser()).isEqualTo(applicationUserBack);
        assertThat(applicationUserBack.getPerson()).isEqualTo(conference);

        conference.applicationUser(null);
        assertThat(conference.getApplicationUser()).isNull();
        assertThat(applicationUserBack.getPerson()).isNull();
    }
}
