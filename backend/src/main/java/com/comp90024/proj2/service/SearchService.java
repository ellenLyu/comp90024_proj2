package com.comp90024.proj2.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface SearchService {

    List<Map<String, Object>> covidPopulation() throws IOException, ParseException;

    Map<String, Map<String, Integer>> tweetBySentiment(String year) throws IOException;

    Map<String, Integer> tweetBySuburbs() throws IOException;

    Map<?, Integer> largeTweetBySuburbs(String suburb, String date) throws IOException;

    Map<String, List<Object>> getDailyNewCases() throws IOException;

    Map<String, Map<String, Integer>> getHashtags(String year) throws IOException;

    Map<String, Integer> getAllCount() throws IOException;

}
