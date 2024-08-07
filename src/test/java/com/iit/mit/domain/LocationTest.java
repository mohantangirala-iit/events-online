package com.iit.mit.domain;

import static com.iit.mit.domain.ConferenceTestSamples.*;
import static com.iit.mit.domain.LocationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iit.mit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void conferenceTest() {
        Location location = getLocationRandomSampleGenerator();
        Conference conferenceBack = getConferenceRandomSampleGenerator();

        location.setConference(conferenceBack);
        assertThat(location.getConference()).isEqualTo(conferenceBack);
        assertThat(conferenceBack.getLocation()).isEqualTo(location);

        location.conference(null);
        assertThat(location.getConference()).isNull();
        assertThat(conferenceBack.getLocation()).isNull();
    }
}
