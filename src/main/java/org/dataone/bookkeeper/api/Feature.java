package org.dataone.bookkeeper.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A Feature describes an aspect of a Product, with optional limits.
 */
public class Feature {

    /* The feature name */
    @NotNull
    @NotEmpty
    private String name;

    /* The feature label */
    @NotNull
    @NotEmpty
    private String label;

    /* The feature description */
    @NotNull
    @NotEmpty
    private String description;

    /* The optional feature quota */
    private Quota quota;

    /**
     * Construct an empty Feature
     */
    public Feature() {
        super();
    }

    /**
     * Construct a Feature
     * @param name
     * @param label
     * @param description
     * @param quota
     */
    public Feature(@NotNull @NotEmpty String name,
                   @NotNull @NotEmpty String label,
                   @NotNull @NotEmpty String description,
                   @NotEmpty Quota quota) {
        this.name = name;
        this.label = label;
        this.description = description;
        this.quota = quota;
    }

    /**
     * Get the feature name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the feature name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the feature label
     * @return label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the feature label
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Get the feature description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the feature description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the feature quota
     * @return quota
     */
    public Quota getQuota() {
        return quota;
    }

    /**
     * Set the feature quota
     * @param quota
     */
    public void setQuota(Quota quota) {
        this.quota = quota;
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
        Feature feature = (Feature) o;
        return Objects.equals(getName(), feature.getName()) &&
            Objects.equals(getLabel(), feature.getLabel()) &&
            Objects.equals(getDescription(), feature.getDescription()) &&
            Objects.equals(getQuota(), feature.getQuota());
    }

    /**
     * Calculate a hash based on all fields
     * @return hashcode
     */
    @Override
    public int hashCode() {

        return Objects.hash(getName(), getLabel(), getDescription(), getQuota());
    }
}
