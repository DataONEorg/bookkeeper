package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.Jackson;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.util.Objects;

/**
 * Quotas represent limits placed on services resources (storage, etc.)
 */
@JsonInclude(Include.NON_NULL)
public class Quota {
    /* The quota id (assigned by db layer) */
    private Integer id;

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
    private Integer softLimit;

    /* The quota hard limit */
    @NotEmpty
    @NotNull
    private Integer hardLimit;

    /* The usage of the quota */
    private Integer usage;

    /* The quota unit */
    @NotEmpty
    @NotNull
    private String unit;

    /* The quota customer id */
    private Integer customerId;

    /* The quota subject id */
    private String subject;

    /**
     * Construct an empty Quota
     */
    public Quota() {
        super();
    }

    /**
     * Construct a Quota from a JSON string
     * @param json
     * @throws IOException
     */
    public Quota(String json) throws IOException {
        super();

        // Return an empty Quota instance when the JSON object is empty
        if ( ! json.equals("{}") ) {

            // Otherwise try to build the Quota
            Quota quota = Jackson.newObjectMapper().readValue(json, Quota.class);
            this.id = quota.id;
            this.object = quota.object;
            this.name = quota.name;
            this.softLimit = quota.softLimit;
            this.hardLimit = quota.hardLimit;
            this.usage = quota.usage;
            this.unit = quota.unit;
            this.customerId = quota.customerId;
            this.subject = quota.subject;
        }
    }

    /**
     * Construct a quota
     * @param id
     * @param object
     * @param name
     * @param softLimit
     * @param hardLimit
     * @param unit
     * @param usage
     * @param customerId
     * @param subject
     */
    public Quota(Integer id, String object, String name, Integer softLimit,
                 Integer hardLimit, Integer usage, String unit,
                 Integer customerId, String subject) {
        if ( id != null ) {
            if ( ! id.equals("") ) {
                this.id = id;
            }
        }
        this.object = object;
        this.name = name;
        this.softLimit = softLimit;
        this.hardLimit = hardLimit;
        this.unit = unit;
        this.usage = usage;
        this.customerId = customerId;
        this.subject = subject;
    }

    /**
     * Get the quota id
     * @return
     */
    @JsonProperty
    public Integer getId() {
        return id;
    }

    /**
     * Set the quota id
     * @param id
     */
    @JsonProperty
    public void setId(Integer id) {
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
    public @NotEmpty @NotNull Integer getSoftLimit() {
        return softLimit;
    }

    /**
     * Set the quota soft limit
     * @param softLimit
     */
    @JsonProperty
    public void setSoftLimit(@NotEmpty @NotNull Integer softLimit) {
        this.softLimit = softLimit;
    }

    /**
     * Get the quota hard limit
     * @return hardLimit
     */
    @JsonProperty
    public @NotEmpty @NotNull Integer getHardLimit() {
        return hardLimit;
    }

    /**
     * Set the quota hard limit
     * @param hardLimit
     */
    @JsonProperty
    public void setHardLimit(@NotEmpty @NotNull Integer hardLimit) {
        this.hardLimit = hardLimit;
    }


    /**
     * Get the quota usage
     * @return usage
     */
    @JsonProperty
    public Integer getUsage() {
        return usage;
    }

    /**
     * Set the quota usage
     * @param usage
     */
    @JsonProperty
    public void setUsage(Integer usage) {
        this.usage = usage;
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
     * Get the customer id
     * @return customerId
     */
    @JsonProperty
    public Integer getCustomerId() {
        return customerId;
    }

    /**
     * Set the customer id
     * @param customerId
     */
    @JsonProperty
    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    /**
     * Get the subject
     * @return subject
     */
    @JsonProperty
    public String getSubject() {
        return subject;
    }

    /**
     * Set the subject
     * @param subject
     */
    @JsonProperty
    public void setSubject(String subject) {
        this.subject = subject;
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
        return Objects.equals(getId(), quota.getId()) &&
            Objects.equals(getObject(), quota.getObject()) &&
            Objects.equals(getName(), quota.getName()) &&
            Objects.equals(getSoftLimit(), quota.getSoftLimit()) &&
            Objects.equals(getHardLimit(), quota.getHardLimit()) &&
            Objects.equals(getUsage(), quota.getUsage()) &&
            Objects.equals(getUnit(), quota.getUnit()) &&
            Objects.equals(getCustomerId(), quota.getCustomerId()) &&
            Objects.equals(getSubject(), quota.getSubject());
    }

    /**
     * Calculate a hash based on all fields
     * @return hashcode
     */
    @Override
    public int hashCode() {

        return Objects.hash(getId(), getObject(), getName(), getSoftLimit(),
            getHardLimit(), getUsage(), getUnit(), getCustomerId(), getSubject());
    }
}
