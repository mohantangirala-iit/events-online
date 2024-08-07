package com.iit.mit.service.criteria;

import com.iit.mit.domain.enumeration.EventType;
import com.iit.mit.domain.enumeration.Language;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.iit.mit.domain.Event} entity. This class is used
 * in {@link com.iit.mit.web.rest.EventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Language
     */
    public static class LanguageFilter extends Filter<Language> {

        public LanguageFilter() {}

        public LanguageFilter(LanguageFilter filter) {
            super(filter);
        }

        @Override
        public LanguageFilter copy() {
            return new LanguageFilter(this);
        }
    }

    /**
     * Class for filtering EventType
     */
    public static class EventTypeFilter extends Filter<EventType> {

        public EventTypeFilter() {}

        public EventTypeFilter(EventTypeFilter filter) {
            super(filter);
        }

        @Override
        public EventTypeFilter copy() {
            return new EventTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter title;

    private StringFilter description;

    private StringFilter audience;

    private StringFilter level;

    private LanguageFilter language;

    private InstantFilter date;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private EventTypeFilter eventType;

    private LongFilter conferenceId;

    private Boolean distinct;

    public EventCriteria() {}

    public EventCriteria(EventCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.title = other.optionalTitle().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.audience = other.optionalAudience().map(StringFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(StringFilter::copy).orElse(null);
        this.language = other.optionalLanguage().map(LanguageFilter::copy).orElse(null);
        this.date = other.optionalDate().map(InstantFilter::copy).orElse(null);
        this.startTime = other.optionalStartTime().map(InstantFilter::copy).orElse(null);
        this.endTime = other.optionalEndTime().map(InstantFilter::copy).orElse(null);
        this.eventType = other.optionalEventType().map(EventTypeFilter::copy).orElse(null);
        this.conferenceId = other.optionalConferenceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EventCriteria copy() {
        return new EventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public Optional<StringFilter> optionalTitle() {
        return Optional.ofNullable(title);
    }

    public StringFilter title() {
        if (title == null) {
            setTitle(new StringFilter());
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getAudience() {
        return audience;
    }

    public Optional<StringFilter> optionalAudience() {
        return Optional.ofNullable(audience);
    }

    public StringFilter audience() {
        if (audience == null) {
            setAudience(new StringFilter());
        }
        return audience;
    }

    public void setAudience(StringFilter audience) {
        this.audience = audience;
    }

    public StringFilter getLevel() {
        return level;
    }

    public Optional<StringFilter> optionalLevel() {
        return Optional.ofNullable(level);
    }

    public StringFilter level() {
        if (level == null) {
            setLevel(new StringFilter());
        }
        return level;
    }

    public void setLevel(StringFilter level) {
        this.level = level;
    }

    public LanguageFilter getLanguage() {
        return language;
    }

    public Optional<LanguageFilter> optionalLanguage() {
        return Optional.ofNullable(language);
    }

    public LanguageFilter language() {
        if (language == null) {
            setLanguage(new LanguageFilter());
        }
        return language;
    }

    public void setLanguage(LanguageFilter language) {
        this.language = language;
    }

    public InstantFilter getDate() {
        return date;
    }

    public Optional<InstantFilter> optionalDate() {
        return Optional.ofNullable(date);
    }

    public InstantFilter date() {
        if (date == null) {
            setDate(new InstantFilter());
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public Optional<InstantFilter> optionalStartTime() {
        return Optional.ofNullable(startTime);
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            setStartTime(new InstantFilter());
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public Optional<InstantFilter> optionalEndTime() {
        return Optional.ofNullable(endTime);
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            setEndTime(new InstantFilter());
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public EventTypeFilter getEventType() {
        return eventType;
    }

    public Optional<EventTypeFilter> optionalEventType() {
        return Optional.ofNullable(eventType);
    }

    public EventTypeFilter eventType() {
        if (eventType == null) {
            setEventType(new EventTypeFilter());
        }
        return eventType;
    }

    public void setEventType(EventTypeFilter eventType) {
        this.eventType = eventType;
    }

    public LongFilter getConferenceId() {
        return conferenceId;
    }

    public Optional<LongFilter> optionalConferenceId() {
        return Optional.ofNullable(conferenceId);
    }

    public LongFilter conferenceId() {
        if (conferenceId == null) {
            setConferenceId(new LongFilter());
        }
        return conferenceId;
    }

    public void setConferenceId(LongFilter conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EventCriteria that = (EventCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(audience, that.audience) &&
            Objects.equals(level, that.level) &&
            Objects.equals(language, that.language) &&
            Objects.equals(date, that.date) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(eventType, that.eventType) &&
            Objects.equals(conferenceId, that.conferenceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, audience, level, language, date, startTime, endTime, eventType, conferenceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitle().map(f -> "title=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalAudience().map(f -> "audience=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalLanguage().map(f -> "language=" + f + ", ").orElse("") +
            optionalDate().map(f -> "date=" + f + ", ").orElse("") +
            optionalStartTime().map(f -> "startTime=" + f + ", ").orElse("") +
            optionalEndTime().map(f -> "endTime=" + f + ", ").orElse("") +
            optionalEventType().map(f -> "eventType=" + f + ", ").orElse("") +
            optionalConferenceId().map(f -> "conferenceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
