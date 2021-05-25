/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"id", "revision"})
public class Covid {

    @JsonProperty("_id")
    private String _id;

    @JsonProperty("_rev")
    private String _revision;

    @JsonProperty("postcode")
    private String postcode;

    @JsonProperty("population")
    private String population;

    @JsonProperty("data_date")
    private String data_date;

    @JsonProperty("Localgovernmentarea")
    private String localGovernmentArea;

    @JsonProperty("active")
    private String active;

    @JsonProperty("cases")
    private String cases;

    @JsonProperty("rate")
    private String rate;

    @JsonProperty("new")
    private String newCases;

    @JsonProperty("band")
    private String band;

    @JsonProperty("file_processed_date")
    private String fileProcessedDate;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_revision() {
        return _revision;
    }

    public void set_revision(String _revision) {
        this._revision = _revision;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getData_date() {
        return data_date;
    }

    public void setData_date(String data_date) {
        this.data_date = data_date;
    }

    public String getLocalGovernmentArea() {
        return localGovernmentArea;
    }

    public void setLocalGovernmentArea(String localGovernmentArea) {
        this.localGovernmentArea = localGovernmentArea;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getNewCases() {
        return newCases;
    }

    public void setNewCases(String newCases) {
        this.newCases = newCases;
    }

    public String getFileProcessedDate() {
        return fileProcessedDate;
    }

    public void setFileProcessedDate(String fileProcessedDate) {
        this.fileProcessedDate = fileProcessedDate;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }
}
