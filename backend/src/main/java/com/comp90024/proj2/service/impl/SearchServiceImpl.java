package com.comp90024.proj2.service.impl;

import com.comp90024.proj2.entity.Covid;
import com.comp90024.proj2.service.SearchService;
import com.comp90024.proj2.util.StringUtils;
import com.comp90024.proj2.view.CovidDaoImpl;
import com.comp90024.proj2.view.DemoDaoImpl;
import com.comp90024.proj2.view.LargeDaoImpl;
import com.comp90024.proj2.view.TweetDaoImpl;
import com.fasterxml.jackson.databind.JsonNode;
import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = Logger.getLogger(SearchServiceImpl.class.getName());

    @Autowired
    private CovidDaoImpl covidDaoImpl;

    @Autowired
    private TweetDaoImpl tweetDaoImpl;

    @Autowired
    private DemoDaoImpl demoDaoImpl;

    @Autowired
    private LargeDaoImpl largeDaoImpl;


    @Override
    public List<List<Float>> groupByDate(String date) {
        List<List<Float>> result = new ArrayList<>();

//        List<Covid> queryRes = covidDaoImpl.findBydata_date(date);
//        System.out.println(queryRes);
//        for (Covid c : queryRes) {
//            System.out.println(c.get_id());
//            List<Float> data = new ArrayList<>();
//            data.add(StringUtils.isNotEmpty(c.getPopulation()) ? Float.parseFloat(c.getPopulation()) : 0);
//            data.add(StringUtils.isNotEmpty(c.getCases()) ? Float.parseFloat(c.getCases()) : 0);
//            result.add(data);
//        }

        return result;
    }

    @Override
    public List<Map<String, Object>> tweetBySentiment(String suburb, String date) {
        List<Map<String, Object>> result = new ArrayList<>();
//        Map<String, Integer> queryRes = tweetDaoImpl.findBySentiment();
//
//        for (String key : queryRes.keySet()) {
//            if (!"total".equals(key)) {
//                Map<String, Object> data = new HashMap<>();
//                data.put("name", key);
//                data.put("y", ((float) queryRes.get(key)) / queryRes.get("total"));
//                result.add(data);
//            }
//        }

        return result;
    }

    @Override
    public Map<String, Integer> tweetBySuburbs() {
//        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Integer> queryRes = demoDaoImpl.getSuburbs();

        return queryRes;
    }


    @Override
    public Map<?, Integer> largeTweetBySuburbs(String suburb, String date) {

        Map<JsonNode, Integer> queryRes = largeDaoImpl.getSuburbsByYears();

        // If specified suburb and date
        if (StringUtils.isNotEmpty(suburb) && StringUtils.isNotEmpty(date)) {
            return queryRes.entrySet().stream().filter(
                    entry -> suburb.equals(entry.getKey().get(0).asText(""))
                            && date.equals(entry.getKey().get(1).asText(""))
            ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        // If specified suburb
        if (StringUtils.isNotEmpty(suburb)) {

            return queryRes.entrySet().stream().filter(
                    entry -> suburb.equals(entry.getKey().get(0).asText(""))
            ).collect(Collectors.toMap(entry -> entry.getKey().get(1).asText().replaceAll("\"", ""),
                    Map.Entry::getValue));
        }

        // If specified date
        if (StringUtils.isNotEmpty(date)) {

            return queryRes.entrySet().stream().filter(
                    entry -> date.equals(entry.getKey().get(1).asText(""))
            ).collect(Collectors.toMap(entry -> entry.getKey().get(0).asText().replaceAll("\"", ""),
                    Map.Entry::getValue));
        }

        return queryRes;
    }

    @Override
    public Map<String, List<Object>> getDailyNewCases() {
        Map<String, List<Object>> res = new LinkedHashMap<>();

        Map<String, Integer> beforeCases = covidDaoImpl.getBeforeDailyCases();
        System.out.println(beforeCases);
        Map<String, Integer> cases = covidDaoImpl.getDailyCases();
        System.out.println(cases);

        beforeCases.putAll(cases);

        res.put("categoryData", List.copyOf(beforeCases.keySet()));
        res.put("valueData", List.copyOf(beforeCases.values()));

        return res;
    }

}
