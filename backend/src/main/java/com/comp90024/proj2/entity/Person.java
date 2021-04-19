package com.comp90024.proj2.entity;

import org.springframework.data.couchbase.core.mapping.Document;

@Document
public class Person {

    private String id;
    private String type;
    private String name;
    private String homeTown;

    // standard getters and setters

}