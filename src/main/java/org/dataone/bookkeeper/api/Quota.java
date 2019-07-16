package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * Quotas represent limits placed on services resources (storage, etc.)
 */
public class Quota {
    /* The quota id (assigned by db layer) */
    private Long id;

    /* The quota object type */
    @NotEmpty
    @NotNull
    @Pattern(regexp = "quota")
    private String object;

    /* The quota name */
    @NotEmpty
    @NotNull
    private String name;

    /* The quota soft limit */
    @NotEmpty
    @NotNull
    private long softLimit;

    /* The quota hard limit */
    @NotEmpty
    @NotNull
    private long hardLimit;

    /* The quota unit */
    @NotEmpty
    @NotNull
    private String unit;

    /**
     * Construct an empty Quota
     */
    public Quota() {
        super();
    }

    public Quota(Long id, String object, String name,
                 int soft_limit, int hard_limit, String unit) {
        if ( id != null ) {
            if ( ! id.equals("") ) {
                this.id = id;
            }
        }
        this.object = object;
        this.name = name;
        this.softLimit = soft_limit;
        this.hardLimit = hard_limit;
        this.unit = unit;
    }

    /**
     * Get the quota id
     * @return
     */
    @JsonProperty
    public Long getId() {
        return id;
    }

    /**
     * Set the quota id
     * @param id
     */
    @JsonProperty
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the quota object type
     * @return object
     */
    @JsonProperty
    public String getObject() {
        return object;
    }

    /**
     * Set the quota object type
     * @param object
     */
    @JsonProperty
    public void setObject(String object) {
        this.object = object;
    }

    /**
     * Get the quota name
     * @return name
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * Set the quota name
     * @param name
     */
    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the quota soft limit
     * @return softLimit
     */
    @JsonProperty
    public @NotEmpty @NotNull long getSoftLimit() {
        return softLimit;
    }

    /**
     * Set the quota soft limit
     * @param soft_limit
     */
    @JsonProperty
    public void setSoftLimit(@NotEmpty @NotNull long soft_limit) {
        this.softLimit = soft_limit;
    }

    /**
     * Get the quota hard limit
     * @return hardLimit
     */
    @JsonProperty
    public @NotEmpty @NotNull long getHardLimit() {
        return hardLimit;
    }

    /**
     * Set the quota hard limit
     * @param hard_limit
     */
    @JsonProperty
    public void setHardLimit(@NotEmpty @NotNull long hard_limit) {
        this.hardLimit = hard_limit;
    }

    /**
     * Get the quota unit
     * @return unit
     */
    @JsonProperty
    public String getUnit() {
        return unit;
    }

    /**
     * Set the quota unit
     * @param unit
     */
    @JsonProperty
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Determine object equality based on the equality of all fields
     * @param o the object to be compared
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quota quota = (Quota) o;
        return getSoftLimit() == quota.getSoftLimit() &&
            getHardLimit() == quota.getHardLimit() &&
            Objects.equals(getId(), quota.getId()) &&
            Objects.equals(getObject(), quota.getObject()) &&
            Objects.equals(getName(), quota.getName()) &&
            Objects.equals(getUnit(), quota.getUnit());
    }

    /**
     * Calculate a hash based on all fields
     * @return hashcode
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getName(), getSoftLimit(), getHardLimit(), getUnit());
    }
}
