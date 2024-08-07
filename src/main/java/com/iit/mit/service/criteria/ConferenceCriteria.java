package com.iit.mit.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.iit.mit.domain.Conference} entity. This class is used
 * in {@link com.iit.mit.web.rest.ConferenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /conferences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConferenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter conferenceName;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private LongFilter locationId;

    private LongFilter eventId;

    private LongFilter applicationUserId;

    private Boolean distinct;

    public ConferenceCriteria() {}

    public ConferenceCriteria(ConferenceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.conferenceName = other.optionalConferenceName().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.locationId = other.optionalLocationId().map(LongFilter::copy).orElse(null);
        this.eventId = other.optionalEventId().map(LongFilter::copy).orElse(null);
        this.applicationUserId = other.optionalApplicationUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ConferenceCriteria copy() {
        return new ConferenceCriteria(this);
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

    public StringFilter getConferenceName() {
        return conferenceName;
    }

    public Optional<StringFilter> optionalConferenceName() {
        return Optional.ofNullable(conferenceName);
    }

    public StringFilter conferenceName() {
        if (conferenceName == null) {
            setConferenceName(new StringFilter());
        }
        return conferenceName;
    }

    public void setConferenceName(StringFilter conferenceName) {
        this.conferenceName = conferenceName;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public Optional<LongFilter> optionalLocationId() {
        return Optional.ofNullable(locationId);
    }

    public LongFilter locationId() {
        if (locationId == null) {
            setLocationId(new LongFilter());
        }
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getEventId() {
        return eventId;
    }

    public Optional<LongFilter> optionalEventId() {
        return Optional.ofNullable(eventId);
    }

    public LongFilter eventId() {
        if (eventId == null) {
            setEventId(new LongFilter());
        }
        return eventId;
    }

    public void setEventId(LongFilter eventId) {
        this.eventId = eventId;
    }

    public LongFilter getApplicationUserId() {
        return applicationUserId;
    }

    public Optional<LongFilter> optionalApplicationUserId() {
        return Optional.ofNullable(applicationUserId);
    }

    public LongFilter applicationUserId() {
        if (applicationUserId == null) {
            setApplicationUserId(new LongFilter());
        }
        return applicationUserId;
    }

    public void setApplicationUserId(LongFilter applicationUserId) {
        this.applicationUserId = applicationUserId;
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
        final ConferenceCriteria that = (ConferenceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(conferenceName, that.conferenceName) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(locationId, that.locationId) &&
            Objects.equals(eventId, that.eventId) &&
            Objects.equals(applicationUserId, that.applicationUserId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, conferenceName, startDate, endDate, locationId, eventId, applicationUserId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConferenceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalConferenceName().map(f -> "conferenceName=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalLocationId().map(f -> "locationId=" + f + ", ").orElse("") +
            optionalEventId().map(f -> "eventId=" + f + ", ").orElse("") +
            optionalApplicationUserId().map(f -> "applicationUserId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
