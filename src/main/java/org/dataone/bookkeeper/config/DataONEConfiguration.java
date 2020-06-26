/*
 * This work was created by participants in the DataONE project, and is
 * jointly copyrighted by participating institutions in DataONE. For
 * more information on DataONE, see our web site at http://dataone.org.
 *
 *   Copyright 2019
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dataone.bookkeeper.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import java.util.List;

/**
 * Configuration for DataONE library settings
 */
public class DataONEConfiguration extends Configuration {

    /* The Coordinating Node base URL */
    private String cnBaseUrl;

    /* The list of subjects will all CRUD permissions */
    private List<String> adminSubjects;

    /* The list of subjects that can perform privileged CRUD operations (e.g. quota create, delete, ..) */
    private List<String> bookkeeperAdminSubjects;

    /* The default trial period in days */
    private long trialDurationDays;
    /**
     * Get the CN base URL
     * @return cnBaseUrl  the CN base URL
     */
    @JsonProperty("cnBaseUrl")
    public String getCnBaseUrl() {
        return cnBaseUrl;
    }

    /**
     * Get the CN base URL
     * @param cnBaseUrl the CN base URL
     */
    @JsonProperty("cnBaseUrl")
    public void setCnBaseUrl(String cnBaseUrl) {
        this.cnBaseUrl = cnBaseUrl;
    }

    /**
     * Get the admin subjects list
     * @return adminSubjects  the list of admin subjects
     */
    @JsonProperty("adminSubjects")
    public List<String> getAdminSubjects() {
        return adminSubjects;
    }

    /**
     * Get the bookkeeper admin subjects list
     * The 'bookkeeperAdmin' list contains subjects that have a higher
     * level of privilege than subjects in the 'adminSubjects' list
     * @return bookkeeperAdminSubjects the list of bookkeeper admin subjects
     */
    @JsonProperty("bookkeeperAdminSubjects")
    public List<String> getBookkeeperAdminSubjects() {
        return bookkeeperAdminSubjects;
    }

    /**
     * Set the admin subjects list
     * @param adminSubjects  the list of admin subjects
     */
    @JsonProperty("adminSubjects")
    public void setAdminSubjects(List<String> adminSubjects) {
        this.adminSubjects = adminSubjects;
    }


    /**
     * Set the bookkeeper admin subjects list
     * @param bookkeeperAdminSubjects the list of bookkeeper admin subjects
     */
    @JsonProperty("bookkeeperAdminSubjects")
    public void setBookkeeperAdminSubjects(List<String> bookkeeperAdminSubjects) {
        this.bookkeeperAdminSubjects = bookkeeperAdminSubjects;
    }

    /**
     * Get the trial duration days
     * @return trialDurationDays the trial duration days
     */
    @JsonProperty("trialDurationDays")
    public long getTrialDurationDays() {
        return trialDurationDays;
    }

    /**
     * Set the trial duration days
     * @param trialDurationDays the trial duration days
     */
    @JsonProperty("trialDurationDays")
    public void setTrialDurationDays(long trialDurationDays) {
        this.trialDurationDays = trialDurationDays;
    }
}
