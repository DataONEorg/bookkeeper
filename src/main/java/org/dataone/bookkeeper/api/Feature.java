package org.dataone.bookkeeper.api;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
    @NotEmpty // Should be null if empty
    private Quota quota;

    /**
     * Construct an empty Feature
     */
    public Feature() {
        super();
    }

    /**
     * Construct a Feature
     */
    public Feature(String name, String label, String description, Quota quota) {

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
}
