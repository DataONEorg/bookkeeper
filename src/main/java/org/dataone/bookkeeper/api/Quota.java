package org.dataone.bookkeeper.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Quotas represent limits placed on services resources (storage, etc.)
 */
public class Quota {
    /* The quota id (assigned by db layer) */
    private String id;

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
    private int softLimit;

    /* The quota hard limit */
    @NotEmpty
    @NotNull
    private int hardLimit;

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

    public Quota(String id, String object, String name,
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
    public String getId() {
        return id;
    }

    /**
     * Set the quota id
     * @param id
     */
    @JsonProperty
    public void setId(String id) {
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
    public long getSoftLimit() {
        return softLimit;
    }

    /**
     * Set the quota soft limit
     * @param soft_limit
     */
    @JsonProperty
    public void setSoftLimit(int soft_limit) {
        this.softLimit = soft_limit;
    }

    /**
     * Get the quota hard limit
     * @return hardLimit
     */
    @JsonProperty
    public long getHardLimit() {
        return hardLimit;
    }

    /**
     * Set the quota hard limit
     * @param hard_limit
     */
    @JsonProperty
    public void setHardLimit(int hard_limit) {
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
}
