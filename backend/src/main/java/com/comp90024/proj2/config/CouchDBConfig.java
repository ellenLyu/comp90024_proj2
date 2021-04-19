package com.comp90024.proj2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;

public class CouchDBConfig extends AbstractCouchbaseConfiguration {

    @Value ("${spring.couchbase.connection-string}")
    private String connectionString;

    @Value ("${spring.couchbase.username}")
    private String username;

    @Value ("${spring.couchbase.password}")
    private String password;

    @Value ("${spring.data.couchbase.bucket-name}")
    private String bucketName;


    @Override
    public String getConnectionString() {
        System.out.println(connectionString);
        return connectionString;
    }

    @Override
    public String getUserName() {
        System.out.println(username);
        return username;
    }

    @Override
    public String getPassword() {
        System.out.println(password);
        return password;
    }

    @Override
    public String getBucketName() {
        System.out.println(bucketName);
        return bucketName;
    }
}
