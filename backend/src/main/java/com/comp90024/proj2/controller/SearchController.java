package com.comp90024.proj2.controller;

import com.alibaba.fastjson.JSONObject;
import com.comp90024.proj2.service.impl.SearchServiceImpl;
import com.comp90024.proj2.util.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    public SearchServiceImpl searchService;

    @RequestMapping(value = "/covid_scatter", method = RequestMethod.POST)
    public CommonResult getCovidScatter(@RequestBody JSONObject requestBody) {

        List<List<Float>> result = searchService.groupByDate(requestBody.getString("data"));
        return CommonResult.success(result, "success");
    }


    @RequestMapping(value = "/tweet_sent_pie", method = RequestMethod.POST)
    public CommonResult getTweetSentiment(@RequestBody JSONObject requestBody) {

        List<Map<String, Object>> result = searchService.tweetBySentiment();
        return CommonResult.success(result, "success");
    }


}