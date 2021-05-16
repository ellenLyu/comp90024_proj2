package com.comp90024.proj2.service.impl;

import com.comp90024.proj2.entity.Covid;
import com.comp90024.proj2.service.SearchService;
import com.comp90024.proj2.util.StringUtils;
import com.comp90024.proj2.view.CovidDaoImpl;
import com.comp90024.proj2.view.TweetDaoImpl;
import org.ektorp.CouchDbConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Logger logger = Logger.getLogger(SearchServiceImpl.class.getName());

    //    @Autowired
//    private CouchDbConnector tweetDbConnector;
//
    @Autowired
    private CouchDbConnector covidDbConnector;

    @Autowired
    private CovidDaoImpl covidDaoImpl;

    @Autowired
    private TweetDaoImpl tweetDaoImpl;


    @Override
    public List<List<Float>> groupByDate(String date) {
        List<List<Float>> result = new ArrayList<>();

//        CouchDbClient dbClient = new CouchDbClient();
//
//        List<Covid> queryRes = dbClient.view("example/by_date")
//                .key(date)
//                .includeDocs(true)
//                .query(Covid.class);

        List<Covid> queryRes = covidDaoImpl.findBydata_date(date);
        System.out.println(queryRes);
        for (Covid c : queryRes) {
            System.out.println(c.get_id());
            List<Float> data = new ArrayList<>();
            data.add(StringUtils.isNotEmpty(c.getPopulation()) ? Float.parseFloat(c.getPopulation()) : 0);
            data.add(StringUtils.isNotEmpty(c.getCases()) ? Float.parseFloat(c.getCases()) : 0);
            result.add(data);
        }

        return result;
    }

    @Override
    public List<List<Float>> getAll() throws IOException {
//        CovidDaoImpl covidDaoImpl = new CovidDaoImpl(covidDbConnector);

        List<Covid> queryRes = covidDaoImpl.getAll();

        for (Covid c : queryRes) {
            System.out.println(c.get_id());
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> tweetBySentiment() {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Integer> queryRes = tweetDaoImpl.findBySentiment();

        for (String key : queryRes.keySet()) {
            if (!"total".equals(key)) {
                Map<String, Object> data = new HashMap<>();
                data.put("name", key);
                data.put("y", ((float) queryRes.get(key)) / queryRes.get("total"));
                result.add(data);
            }
        }

        return result;
    }
}
