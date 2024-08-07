package com.iit.mit.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EventCriteriaTest {

    @Test
    void newEventCriteriaHasAllFiltersNullTest() {
        var eventCriteria = new EventCriteria();
        assertThat(eventCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void eventCriteriaFluentMethodsCreatesFiltersTest() {
        var eventCriteria = new EventCriteria();

        setAllFilters(eventCriteria);

        assertThat(eventCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void eventCriteriaCopyCreatesNullFilterTest() {
        var eventCriteria = new EventCriteria();
        var copy = eventCriteria.copy();

        assertThat(eventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(eventCriteria)
        );
    }

    @Test
    void eventCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var eventCriteria = new EventCriteria();
        setAllFilters(eventCriteria);

        var copy = eventCriteria.copy();

        assertThat(eventCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(eventCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var eventCriteria = new EventCriteria();

        assertThat(eventCriteria).hasToString("EventCriteria{}");
    }

    private static void setAllFilters(EventCriteria eventCriteria) {
        eventCriteria.id();
        eventCriteria.title();
        eventCriteria.description();
        eventCriteria.audience();
        eventCriteria.level();
        eventCriteria.language();
        eventCriteria.date();
        eventCriteria.startTime();
        eventCriteria.endTime();
        eventCriteria.eventType();
        eventCriteria.conferenceId();
        eventCriteria.distinct();
    }

    private static Condition<EventCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getAudience()) &&
                condition.apply(criteria.getLevel()) &&
                condition.apply(criteria.getLanguage()) &&
                condition.apply(criteria.getDate()) &&
                condition.apply(criteria.getStartTime()) &&
                condition.apply(criteria.getEndTime()) &&
                condition.apply(criteria.getEventType()) &&
                condition.apply(criteria.getConferenceId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EventCriteria> copyFiltersAre(EventCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getAudience(), copy.getAudience()) &&
                condition.apply(criteria.getLevel(), copy.getLevel()) &&
                condition.apply(criteria.getLanguage(), copy.getLanguage()) &&
                condition.apply(criteria.getDate(), copy.getDate()) &&
                condition.apply(criteria.getStartTime(), copy.getStartTime()) &&
                condition.apply(criteria.getEndTime(), copy.getEndTime()) &&
                condition.apply(criteria.getEventType(), copy.getEventType()) &&
                condition.apply(criteria.getConferenceId(), copy.getConferenceId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
