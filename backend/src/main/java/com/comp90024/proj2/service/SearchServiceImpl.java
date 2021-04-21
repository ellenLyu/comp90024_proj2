package com.comp90024.proj2.service;

import com.comp90024.proj2.entity.Tweet;
import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SearchServiceImpl implements SearchService{

    private static final Logger logger = Logger.getLogger(SearchServiceImpl.class.getName());

    @Autowired
    private CouchDbConnector couchDbConnector;

    @Override
    public void createDoc() {

        Tweet tweet = new Tweet("sid - val", null, "type - val",
                "text - val", "user - val");

        couchDbConnector.create(tweet.getId(), tweet);

        logger.log(Level.INFO, "Doc: " + tweet.getId() + " Created");
    }
}
