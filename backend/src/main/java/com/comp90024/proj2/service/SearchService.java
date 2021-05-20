package com.comp90024.proj2.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {

    List<List<Float>> groupByDate(String date) throws IOException;

    List<Map<String, Object>> tweetBySentiment() throws IOException;

    Map<String, Integer> tweetBySuburbs() throws IOException;

    Map<?, Integer> largeTweetBySuburbs(String suburb, String date) throws IOException;

    Map<String, List<Object>> getDailyNewCases() throws IOException;

}
