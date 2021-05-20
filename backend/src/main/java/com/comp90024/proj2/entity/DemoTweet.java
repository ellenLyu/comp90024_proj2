package com.comp90024.proj2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

public class DemoTweet {

    @JsonProperty("_id")
    private String _id;

    @JsonProperty("_rev")
    private String _revision;

    @JsonProperty("text")
    private String text;

    @JsonProperty("sentiment")
    private String sentiment;

    @JsonProperty("suburb")
    private String suburb;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("retweet_count")
    private int retweetCount;

    @JsonProperty("favorite_count")
    private int favoriteCount;

    @JsonProperty("favorite_count")
    private int postcode;

    @JsonProperty("coordinates")
    private HashMap<String, Object> coordinates;

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

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public HashMap<String, Object> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(HashMap<String, Object> coordinates) {
        this.coordinates = coordinates;
    }
}
