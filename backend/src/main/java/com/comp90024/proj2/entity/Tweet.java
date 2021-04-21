package com.comp90024.proj2.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tweet {

    @JsonProperty(value="_id")
    private String id;

    @JsonProperty(value="_rev")
    private String revision;

    private String type;
    private String text;
    private String user;

    public Tweet(String id, String revision, String type, String text, String user) {
        this.id = id;
        this.revision = revision;
        this.type = type;
        this.text = text;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    // standard getters and setters

}