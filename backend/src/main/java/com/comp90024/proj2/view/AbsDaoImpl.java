/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.view;

import com.comp90024.proj2.entity.DemoTweet;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class AbsDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(AbsDaoImpl.class);

    @Autowired
    private CouchDbConnector absDbConnector;


    /**
     * Get abs data
     */
    public ViewResult getAbsData(String year) {
        ViewQuery query = new ViewQuery().designDocId("_design/example")
                .viewName("by_year_suburb")
                .startKey(Arrays.asList(year, "a"))
                .endKey(Arrays.asList(year, "z"));

        return absDbConnector.queryView(query);
    }


    public Integer getAllCount() {
        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("get_all_count");

        ViewResult result = absDbConnector.queryView(query);

        if (result.getRows().get(0) != null) {
            return result.getRows().get(0).getValueAsInt();
        }

        return 0;
    }

}
