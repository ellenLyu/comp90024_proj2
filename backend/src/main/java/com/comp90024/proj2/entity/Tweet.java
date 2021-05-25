/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Tweet {

    @JsonProperty("_id")
    private String _id;

    @JsonProperty("_rev")
    private String _revision;

    @JsonProperty("text")
    private String text;

    @JsonProperty("sentiment")
    private String sentiment;

    @JsonProperty("coordinates")
    private List<Double> coordinates;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }
}