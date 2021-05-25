/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.view;

import com.comp90024.proj2.entity.Tweet;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
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
public class TweetDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(TweetDaoImpl.class);

    @Autowired
    private CouchDbConnector tweetDbConnector;

    @View(name="get_all_count")
    public Integer getAllCount() {
        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("get_all_count");

        ViewResult result = tweetDbConnector.queryView(query);

        if (result.getRows().get(0) != null) {
            return result.getRows().get(0).getValueAsInt();
        }

        return 0;
    }

}