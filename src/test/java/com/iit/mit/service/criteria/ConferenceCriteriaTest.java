package com.iit.mit.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ConferenceCriteriaTest {

    @Test
    void newConferenceCriteriaHasAllFiltersNullTest() {
        var conferenceCriteria = new ConferenceCriteria();
        assertThat(conferenceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void conferenceCriteriaFluentMethodsCreatesFiltersTest() {
        var conferenceCriteria = new ConferenceCriteria();

        setAllFilters(conferenceCriteria);

        assertThat(conferenceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void conferenceCriteriaCopyCreatesNullFilterTest() {
        var conferenceCriteria = new ConferenceCriteria();
        var copy = conferenceCriteria.copy();

        assertThat(conferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(conferenceCriteria)
        );
    }

    @Test
    void conferenceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var conferenceCriteria = new ConferenceCriteria();
        setAllFilters(conferenceCriteria);

        var copy = conferenceCriteria.copy();

        assertThat(conferenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(conferenceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var conferenceCriteria = new ConferenceCriteria();

        assertThat(conferenceCriteria).hasToString("ConferenceCriteria{}");
    }

    private static void setAllFilters(ConferenceCriteria conferenceCriteria) {
        conferenceCriteria.id();
        conferenceCriteria.conferenceName();
        conferenceCriteria.startDate();
        conferenceCriteria.endDate();
        conferenceCriteria.locationId();
        conferenceCriteria.eventId();
        conferenceCriteria.applicationUserId();
        conferenceCriteria.distinct();
    }

    private static Condition<ConferenceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getConferenceName()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getLocationId()) &&
                condition.apply(criteria.getEventId()) &&
                condition.apply(criteria.getApplicationUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ConferenceCriteria> copyFiltersAre(ConferenceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getConferenceName(), copy.getConferenceName()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getLocationId(), copy.getLocationId()) &&
                condition.apply(criteria.getEventId(), copy.getEventId()) &&
                condition.apply(criteria.getApplicationUserId(), copy.getApplicationUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
