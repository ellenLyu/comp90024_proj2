package com.comp90024.proj2.config;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchDBConfig {

    @Bean
    public CouchDbConnector couchDbConnector(@Value("${couchdb.host}") String host,
                                             @Value("${couchdb.port}") String port,
                                             @Value("${couchdb.username}") String username,
                                             @Value("${couchdb.password}") String password,
                                             @Value("${couchdb.database}") String database) throws Exception {
        HttpClient httpClient = new StdHttpClient.Builder().url(host + ":" + port)
                .username(username).connectionTimeout(10000).socketTimeout(10000)
                .password(password).build();
        CouchDbInstance couchDbInstance = new StdCouchDbInstance(httpClient);
        CouchDbConnector couchDbConnector = new StdCouchDbConnector(database, couchDbInstance);
        couchDbConnector.createDatabaseIfNotExists();

        return couchDbConnector;
    }
}
