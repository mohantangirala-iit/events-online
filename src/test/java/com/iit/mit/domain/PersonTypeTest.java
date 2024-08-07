package com.iit.mit.domain;

import static com.iit.mit.domain.ApplicationUserTestSamples.*;
import static com.iit.mit.domain.PersonTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.iit.mit.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonType.class);
        PersonType personType1 = getPersonTypeSample1();
        PersonType personType2 = new PersonType();
        assertThat(personType1).isNotEqualTo(personType2);

        personType2.setId(personType1.getId());
        assertThat(personType1).isEqualTo(personType2);

        personType2 = getPersonTypeSample2();
        assertThat(personType1).isNotEqualTo(personType2);
    }

    @Test
    void applicationUserTest() {
        PersonType personType = getPersonTypeRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        personType.setApplicationUser(applicationUserBack);
        assertThat(personType.getApplicationUser()).isEqualTo(applicationUserBack);
        assertThat(applicationUserBack.getPersontype()).isEqualTo(personType);

        personType.applicationUser(null);
        assertThat(personType.getApplicationUser()).isNull();
        assertThat(applicationUserBack.getPersontype()).isNull();
    }
}
