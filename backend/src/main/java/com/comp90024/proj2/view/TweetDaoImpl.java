package com.comp90024.proj2.view;

import com.comp90024.proj2.entity.Tweet;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@DependsOn("tweetDbConnector")
public class TweetDaoImpl extends CouchDbRepositorySupport<Tweet> {

    private static final Logger logger = LoggerFactory.getLogger(TweetDaoImpl.class);

    @Autowired
    public TweetDaoImpl(@Qualifier("tweetDbConnector") CouchDbConnector couchdb) {
        super(Tweet.class, couchdb);
        try {
            initStandardDesignDocument();
        } catch (NullPointerException e) {
            logger.debug("View TweetDaoImpl is existing.");
        }
    }

}