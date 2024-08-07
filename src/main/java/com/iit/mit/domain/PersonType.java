package com.iit.mit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iit.mit.domain.enumeration.Level;
import com.iit.mit.domain.enumeration.Role;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonType.
 */
@Entity
@Table(name = "person_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PersonType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "job_title")
    private Long jobTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @JsonIgnoreProperties(value = { "persontype", "person", "internalUser" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "persontype")
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobTitle() {
        return this.jobTitle;
    }

    public PersonType jobTitle(Long jobTitle) {
        this.setJobTitle(jobTitle);
        return this;
    }

    public void setJobTitle(Long jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Role getRole() {
        return this.role;
    }

    public PersonType role(Role role) {
        this.setRole(role);
        return this;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Level getLevel() {
        return this.level;
    }

    public PersonType level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        if (this.applicationUser != null) {
            this.applicationUser.setPersontype(null);
        }
        if (applicationUser != null) {
            applicationUser.setPersontype(this);
        }
        this.applicationUser = applicationUser;
    }

    public PersonType applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonType)) {
            return false;
        }
        return getId() != null && getId().equals(((PersonType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonType{" +
            "id=" + getId() +
            ", jobTitle=" + getJobTitle() +
            ", role='" + getRole() + "'" +
            ", level='" + getLevel() + "'" +
            "}";
    }
}
