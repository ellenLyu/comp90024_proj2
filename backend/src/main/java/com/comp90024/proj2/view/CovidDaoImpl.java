package com.comp90024.proj2.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.ektorp.CouchDbConnector;
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

        System.out.println(res);
        return res;
    }


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
}
