/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.ektorp.CouchDbConnector;
import org.ektorp.DbInfo;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CovidDaoImpl{

    @Autowired
    private CouchDbConnector covidDbConnector;

    @Autowired
    private CouchDbConnector covidBeforeDbConnector;


    private static final Logger logger = LoggerFactory.getLogger(CovidDaoImpl.class);


    /**
     * ~ 2021/05/17
     * @return
     */
    public Map<String, Integer> getBeforeDailyCases() {
        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("get_daily_new").group(true);
        ViewResult result = covidBeforeDbConnector.queryView(query);

        List<ViewResult.Row> byDate = result.getRows();

        Map<String, Integer> res = new LinkedHashMap<>();
        for (ViewResult.Row row : byDate) {
            try {
                res.put(row.getKey(), Integer.parseInt(row.getValue()));
            } catch (NumberFormatException e) {
                logger.debug("Value of " + row.getKey() + " is not numerical.");
            }
        }

        return res;
    }


    /**
     * 2021/05/17~
     * @return
     */
    public Map<String, Integer> getDailyCases() {
        ViewQuery query = new ViewQuery().designDocId("_design/example")
                .viewName("get_daily_new").group(true);
        ViewResult result = covidDbConnector.queryView(query);

        List<ViewResult.Row> byDate = result.getRows();

        Map<String, Integer> res = new LinkedHashMap<>();
        for (ViewResult.Row row : byDate) {
            try {
                Date key = new SimpleDateFormat("dd/MM/yyyy").parse(row.getKey());
                SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd");
                res.put(format.format(key), Integer.parseInt(row.getValue()));
            } catch (NumberFormatException | ParseException e) {
                logger.debug("Value of " + row.getKey() + " is not numerical.");
            }
        }

        return res;
    }


    public List<ViewResult.Row> getCasesPopulation(String date) {
        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("by_date").key(date);

        ViewResult result = covidDbConnector.queryView(query);
        List<ViewResult.Row> byDate = result.getRows();

        return byDate;
    }


    public Integer getCovidAllCount() {
        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("get_all_count");

        ViewResult result = covidDbConnector.queryView(query);

        if (result.getRows().get(0) != null) {
            return result.getRows().get(0).getValueAsInt();
        }

        return 0;
    }

    public Integer getCovidBeforeAllCount() {
        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("get_all_count");

        ViewResult result = covidBeforeDbConnector.queryView(query);

        if (result.getRows().get(0) != null) {
            return result.getRows().get(0).getValueAsInt();
        }

        return 0;
    }
}
