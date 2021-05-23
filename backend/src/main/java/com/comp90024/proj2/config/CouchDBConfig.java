package com.comp90024.proj2.config;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchDBConfig {


    @Bean
    public CouchDbInstance couchDbInstance(@Value("${couchdb.host}") String host,
                                           @Value("${couchdb.port}") String port,
                                           @Value("${couchdb.username}") String username,
                                           @Value("${couchdb.password}") String password) throws Exception {
        HttpClient httpClient = new StdHttpClient.Builder().url(host + ":" + port)
                .username(username).connectionTimeout(10000).socketTimeout(10000)
                .password(password).build();

        return new StdCouchDbInstance(httpClient);
    }

    @Bean
    @Autowired
    public CouchDbConnector covidDbConnector(CouchDbInstance couchDbInstance,
                                             @Value("${couchdb.database.covid}") String database) throws Exception {

        CouchDbConnector couchDbConnector = new StdCouchDbConnector(database, couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

        return couchDbConnector;
    }


    @Bean
    @Autowired
    public CouchDbConnector covidBeforeDbConnector(CouchDbInstance couchDbInstance,
                                             @Value("${couchdb.database.covid.before}") String database) throws Exception {

        CouchDbConnector couchDbConnector = new StdCouchDbConnector(database, couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

        return couchDbConnector;
    }

    @Bean
    @Autowired
    public CouchDbConnector tweetDbConnector(CouchDbInstance couchDbInstance,
                                             @Value("${couchdb.database.tweets}") String database) throws Exception {

        CouchDbConnector couchDbConnector = new StdCouchDbConnector(database, couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

        return couchDbConnector;
    }

    @Bean
    @Autowired
    public CouchDbConnector demoDbConnector(CouchDbInstance couchDbInstance,
                                             @Value("${couchdb.database.demo}") String database) throws Exception {

        CouchDbConnector couchDbConnector = new StdCouchDbConnector(database, couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

        return couchDbConnector;
    }

    @Bean
    @Autowired
    public CouchDbConnector largeDbConnector(CouchDbInstance couchDbInstance,
                                            @Value("${couchdb.database.large}") String database) throws Exception {

        CouchDbConnector couchDbConnector = new StdCouchDbConnector(database, couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

        return couchDbConnector;
    }

    @Bean
    @Autowired
    public CouchDbConnector absDbConnector(CouchDbInstance couchDbInstance,
                                             @Value("${couchdb.database.abs}") String database) throws Exception {

        CouchDbConnector couchDbConnector = new StdCouchDbConnector(database, couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

        return couchDbConnector;
    }
}
