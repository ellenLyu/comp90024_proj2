package com.comp90024.proj2.controller;

import com.alibaba.fastjson.JSONObject;
import com.comp90024.proj2.service.impl.SearchServiceImpl;
import com.comp90024.proj2.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    public SearchServiceImpl searchService;

    @RequestMapping(value = "/covid_scatter", method = RequestMethod.POST)
    public CommonResult getCovidScatter(@RequestBody JSONObject requestBody) throws ParseException {

        List<Map<String, Object>> result = searchService.covidPopulation();
        return CommonResult.success(result, "success");
    }


    @RequestMapping(value = "/tweet_sent_pie", method = RequestMethod.POST)
    public CommonResult getTweetSentiment(@RequestBody JSONObject requestBody) {

        List<Map<String, Object>> result = searchService.tweetBySentiment(
                requestBody.getString("suburb"), requestBody.getString("year"));
        return CommonResult.success(result, "success");
    }

    @RequestMapping(value = "/by_suburbs", method = RequestMethod.POST)
    public CommonResult getBySuburbs(@RequestBody JSONObject requestBody) {

        Map<String, Integer> result = searchService.tweetBySuburbs();
        return CommonResult.success(result, "success");
    }

    @RequestMapping(value = "/large_by_suburbs", method = RequestMethod.POST)
    public CommonResult getLargeBySuburbs(@RequestBody JSONObject requestBody) {

        Map<?, Integer> result = searchService.largeTweetBySuburbs(
                requestBody.getString("suburb"), requestBody.getString("year"));

        return CommonResult.success(result, "success");
    }

    @RequestMapping(value = "/daily_new", method = RequestMethod.POST)
    public CommonResult getDailyNew(@RequestBody JSONObject requestBody) {

        Map<String, List<Object>> result = searchService.getDailyNewCases();

        return CommonResult.success(result, "success");
    }



}
