package com.iit.mit.service.criteria;

import com.iit.mit.domain.enumeration.Region;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.iit.mit.domain.Location} entity. This class is used
 * in {@link com.iit.mit.web.rest.LocationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /locations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Region
     */
    public static class RegionFilter extends Filter<Region> {

        public RegionFilter() {}

        public RegionFilter(RegionFilter filter) {
            super(filter);
        }

        @Override
        public RegionFilter copy() {
            return new RegionFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter streetAddress;

    private StringFilter postalCode;

    private StringFilter city;

    private StringFilter stateProvince;

    private StringFilter countryName;

    private RegionFilter region;

    private LongFilter conferenceId;

    private Boolean distinct;

    public LocationCriteria() {}

    public LocationCriteria(LocationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.streetAddress = other.optionalStreetAddress().map(StringFilter::copy).orElse(null);
        this.postalCode = other.optionalPostalCode().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.stateProvince = other.optionalStateProvince().map(StringFilter::copy).orElse(null);
        this.countryName = other.optionalCountryName().map(StringFilter::copy).orElse(null);
        this.region = other.optionalRegion().map(RegionFilter::copy).orElse(null);
        this.conferenceId = other.optionalConferenceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LocationCriteria copy() {
        return new LocationCriteria(this);
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

    public StringFilter getStreetAddress() {
        return streetAddress;
    }

    public Optional<StringFilter> optionalStreetAddress() {
        return Optional.ofNullable(streetAddress);
    }

    public StringFilter streetAddress() {
        if (streetAddress == null) {
            setStreetAddress(new StringFilter());
        }
        return streetAddress;
    }

    public void setStreetAddress(StringFilter streetAddress) {
        this.streetAddress = streetAddress;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public Optional<StringFilter> optionalPostalCode() {
        return Optional.ofNullable(postalCode);
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            setPostalCode(new StringFilter());
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getStateProvince() {
        return stateProvince;
    }

    public Optional<StringFilter> optionalStateProvince() {
        return Optional.ofNullable(stateProvince);
    }

    public StringFilter stateProvince() {
        if (stateProvince == null) {
            setStateProvince(new StringFilter());
        }
        return stateProvince;
    }

    public void setStateProvince(StringFilter stateProvince) {
        this.stateProvince = stateProvince;
    }

    public StringFilter getCountryName() {
        return countryName;
    }

    public Optional<StringFilter> optionalCountryName() {
        return Optional.ofNullable(countryName);
    }

    public StringFilter countryName() {
        if (countryName == null) {
            setCountryName(new StringFilter());
        }
        return countryName;
    }

    public void setCountryName(StringFilter countryName) {
        this.countryName = countryName;
    }

    public RegionFilter getRegion() {
        return region;
    }

    public Optional<RegionFilter> optionalRegion() {
        return Optional.ofNullable(region);
    }

    public RegionFilter region() {
        if (region == null) {
            setRegion(new RegionFilter());
        }
        return region;
    }

    public void setRegion(RegionFilter region) {
        this.region = region;
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
        final LocationCriteria that = (LocationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(streetAddress, that.streetAddress) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(city, that.city) &&
            Objects.equals(stateProvince, that.stateProvince) &&
            Objects.equals(countryName, that.countryName) &&
            Objects.equals(region, that.region) &&
            Objects.equals(conferenceId, that.conferenceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, streetAddress, postalCode, city, stateProvince, countryName, region, conferenceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStreetAddress().map(f -> "streetAddress=" + f + ", ").orElse("") +
            optionalPostalCode().map(f -> "postalCode=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalStateProvince().map(f -> "stateProvince=" + f + ", ").orElse("") +
            optionalCountryName().map(f -> "countryName=" + f + ", ").orElse("") +
            optionalRegion().map(f -> "region=" + f + ", ").orElse("") +
            optionalConferenceId().map(f -> "conferenceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
