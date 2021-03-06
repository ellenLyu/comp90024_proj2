/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.view;


import com.comp90024.proj2.entity.DemoTweet;
import com.fasterxml.jackson.databind.JsonNode;
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@DependsOn("largeDbConnector")
public class LargeDaoImpl extends CouchDbRepositorySupport<DemoTweet> {

    private static final Logger logger = LoggerFactory.getLogger(LargeDaoImpl.class);

    @Autowired
    public LargeDaoImpl(@Qualifier("largeDbConnector") CouchDbConnector couchdb) {
        super(DemoTweet.class, couchdb);

        try {
            initStandardDesignDocument();
        } catch (NullPointerException e) {
            logger.debug("View LargeDaoImpl is existing.");
        }
    }


//    @View(name="suburbs_year", map="function (doc) { if (doc.suburb) { var date = doc.created_at.split(\" \"); emit([doc.suburb, date[5]], 1); } }", reduce = "_sum")
    @View(name="suburbs_year")
    public Map<JsonNode, Integer> getSuburbsByYears() {
        Map<JsonNode, Integer> res = new HashMap<>();

        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("suburbs_year").group(true);
        ViewResult result = db.queryView(query);

        List<ViewResult.Row> bySuburbs = result.getRows();
        for (ViewResult.Row row : bySuburbs) {
            res.put(row.getKeyAsNode(), Integer.parseInt(row.getValue()));
        }

        return res;
    }


    @View(name="sentiment_by_year_suburb")
    public ViewResult getSentiments(String year) {

        ViewQuery query = new ViewQuery().designDocId("_design/example")
                .viewName("sentiment_by_year_suburb")
                .startKey(Arrays.asList(year, "a", "n"))
                .endKey(Arrays.asList(year, "z", "p")).group(true);

        return db.queryView(query);
    }


    @View(name="hashtags_by_year_suburb")
    public ViewResult getHashtags(String year) {

        ViewQuery query = new ViewQuery().designDocId("_design/example")
                .viewName("hashtags_by_year_suburb")
                .startKey(Arrays.asList(year, "a"))
                .endKey(Arrays.asList(year, "z")).group(true);

        return db.queryView(query);
    }

    @View(name="get_all_count")
    public Integer getAllCount() {
        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("get_all_count");

        ViewResult result = db.queryView(query);

        if (result.getRows().get(0) != null) {
            return result.getRows().get(0).getValueAsInt();
        }

        return 0;
    }

}
