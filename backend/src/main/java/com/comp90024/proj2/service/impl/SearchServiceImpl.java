package com.comp90024.proj2.service.impl;

import com.comp90024.proj2.service.SearchService;
import com.comp90024.proj2.util.StringUtils;
import com.comp90024.proj2.view.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.ektorp.ViewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@EnableCaching
@EnableAsync(proxyTargetClass=true)
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = Logger.getLogger(SearchServiceImpl.class.getName());

    @Autowired
    private Environment env;

    @Autowired
    private CovidDaoImpl covidDaoImpl;

    @Autowired
    private TweetDaoImpl tweetDaoImpl;

    @Autowired
    private DemoDaoImpl demoDaoImpl;

    @Autowired
    private LargeDaoImpl largeDaoImpl;

    @Autowired
    private AbsDaoImpl absDaoImpl;


    @Override
    public List<Map<String, Object>> covidPopulation() throws ParseException {
        List<Map<String, Object>> result = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        Date key;
        if (now.getHour() < 15) {
            key = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(LocalDate.now().minusDays(2)));
        } else {
            key = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(LocalDate.now().minusDays(1)));
        }

//        Date key = new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-19");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        List<ViewResult.Row> queryRes = covidDaoImpl.getCasesPopulation(format.format(key));

        for (ViewResult.Row row : queryRes) {
            JsonNode value = row.getValueAsNode();

            Map<String, Object> data = new HashMap<>();

            // data
            data.put("data", Arrays.asList(StringUtils.isNotEmpty(value.get(0).asText()) ? Float.parseFloat(value.get(0).asText()) : 0,
                    StringUtils.isNotEmpty(value.get(1).asText()) ? Float.parseFloat(value.get(1).asText()) : 0));
            // color
            data.put("color", "rgba(223, 83, 83, .5)");
            // label
            data.put("name", value.get(2));
            result.add(data);
        }

        return result;
    }

    @Override
    public Map<String, Map<String, Integer>> tweetBySentiment(String year) {
        Map<String, Map<String, Integer>> result = new HashMap<>();
        ViewResult queryRes = largeDaoImpl.getSentiments(year);

        List<ViewResult.Row> bySuburbs = queryRes.getRows();
        for (ViewResult.Row row : bySuburbs) {
            JsonNode keyNode = row.getKeyAsNode();
            Map<String, Integer> bySuburb;
            if (!result.containsKey(keyNode.get(1).asText())) {
                bySuburb = new HashMap<>();
                result.put(keyNode.get(1).asText(), bySuburb);
            } else {
                bySuburb = result.get(keyNode.get(1).asText());
            }
            bySuburb.put(keyNode.get(2).asText(), Integer.parseInt(row.getValue()));
        }

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
        Map<String, Integer> cases = covidDaoImpl.getDailyCases();

        beforeCases.putAll(cases);

        res.put("categoryData", List.copyOf(beforeCases.keySet()));
        res.put("valueData", List.copyOf(beforeCases.values()));

        return res;
    }

    @Override
    @Cacheable(cacheNames = "hashtags", key = "#year")
    public Map<String, LinkedHashMap<String, Integer>> getHashtags(String year) {
        logger.info("getHashtags " + year);
        Map<String, LinkedHashMap<String, Integer>> result = new HashMap<>();

        ViewResult queryRes = largeDaoImpl.getHashtags(year);

        List<ViewResult.Row> byYear = queryRes.getRows();

        for (ViewResult.Row row : byYear) {
            JsonNode keyNode = row.getKeyAsNode();

            LinkedHashMap<String, Integer> bySuburb;
            if (!result.containsKey(keyNode.get(1).asText())) {
                bySuburb = new LinkedHashMap<>();
                result.put(keyNode.get(1).asText(), bySuburb);
            } else {
                bySuburb = result.get(keyNode.get(1).asText());
            }

            bySuburb.put(keyNode.get(2).asText(), Integer.parseInt(row.getValue()));
        }

         for (String suburb : result.keySet()) {
             LinkedHashMap<String, Integer> bySuburb = result.get(suburb);
             LinkedHashMap<String, Integer> sortedBySuburb = new LinkedHashMap<>();

             bySuburb.entrySet().stream()
                     .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                     .forEachOrdered(x -> sortedBySuburb.put(x.getKey(), x.getValue()));

             bySuburb = new LinkedHashMap<>();
             int count = 0;
             for (Map.Entry<String, Integer> entry : sortedBySuburb.entrySet()) {
                 bySuburb.put(entry.getKey(), entry.getValue());
                 count++;
                 if (count == 20) {
                     result.put(suburb, bySuburb);
                     break;
                 }
             }
             result.put(suburb, bySuburb);
         }

        return result;
    }

    @Override
    public Map<String, Integer> getAllCount() {
        Map<String, Integer> res = new HashMap<>();

        res.put(env.getProperty("couchdb.database.covid"), covidDaoImpl.getCovidAllCount());
        res.put(env.getProperty("couchdb.database.covid.before"), covidDaoImpl.getCovidBeforeAllCount());
        res.put(env.getProperty("couchdb.database.large"), largeDaoImpl.getAllCount());
        res.put(env.getProperty("couchdb.database.abs"), absDaoImpl.getAllCount());


        return res;
    }

    @Override
    public Map<String, Map<String, Map<String, Float>>> getAbs(String year) {
        Map<String, Map<String, Map<String, Float>>> result = new HashMap<>();

        // Add the measures
        result.put("Australian citizen (%)", new HashMap<>());
        result.put("Speaks a Language Other Than English at Home (%)", new HashMap<>());
        result.put("Median employee income ($)", new HashMap<>());
        result.put("Completed Year 12 or equivalent (%)", new HashMap<>());

        ViewResult queryRes = absDaoImpl.getAbsData(year);

        List<ViewResult.Row> bySuburbs = queryRes.getRows();
        for (ViewResult.Row row : bySuburbs) {
            JsonNode keyNode = row.getKeyAsNode();
            String measure = keyNode.get(2).asText();

            if (!result.get(measure).containsKey(keyNode.get(1).asText())) {
                result.get(measure).put(keyNode.get(1).asText(), new HashMap<>());
            }

            if ("Australian citizen (%)".equals(measure)) {
                result.get(measure).get(keyNode.get(1).asText()).put("Australian", Float.parseFloat(row.getValue()));
                result.get(measure).get(keyNode.get(1).asText()).put("International", 100 - Float.parseFloat(row.getValue()));
            } else if (measure.endsWith("Speaks a Language Other Than English at Home (%)")) {
                result.get(measure).get(keyNode.get(1).asText()).put("International", Float.parseFloat(row.getValue()));
                result.get(measure).get(keyNode.get(1).asText()).put("English", 100 - Float.parseFloat(row.getValue()));
            } else if (measure.endsWith("Completed Year 12 or equivalent (%)")) {
                result.get(measure).get(keyNode.get(1).asText()).put("Completed", Float.parseFloat(row.getValue()));
                result.get(measure).get(keyNode.get(1).asText()).put("Incomplete", 100 - Float.parseFloat(row.getValue()));
            } else if (measure.endsWith("Median employee income ($)")) {
                result.get(measure).get(keyNode.get(1).asText()).put("Median", Float.parseFloat(row.getValue()));
            }
        }

        return result;
    }

    @CacheEvict(cacheNames = "hashtags", allEntries = true)
    public void clearCache() {
        logger.info("Cache 'hashtags' has been cleared");
    }
}