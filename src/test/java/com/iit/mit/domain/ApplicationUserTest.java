package com.iit.mit.domain;

import static com.iit.mit.domain.ApplicationUserTestSamples.*;
import static com.iit.mit.domain.ConferenceTestSamples.*;
import static com.iit.mit.domain.PersonTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iit.mit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void persontypeTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        PersonType personTypeBack = getPersonTypeRandomSampleGenerator();

        applicationUser.setPersontype(personTypeBack);
        assertThat(applicationUser.getPersontype()).isEqualTo(personTypeBack);

        applicationUser.persontype(null);
        assertThat(applicationUser.getPersontype()).isNull();
    }

    @Test
    void personTest() {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        Conference conferenceBack = getConferenceRandomSampleGenerator();

        applicationUser.setPerson(conferenceBack);
        assertThat(applicationUser.getPerson()).isEqualTo(conferenceBack);

        applicationUser.person(null);
        assertThat(applicationUser.getPerson()).isNull();
    }
}
