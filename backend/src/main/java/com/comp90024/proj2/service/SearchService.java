/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface SearchService {

    List<Map<String, Object>> covidPopulation() throws IOException, ParseException;

    Map<String, Map<String, Integer>> tweetBySentiment(String year) throws IOException;

    Map<String, Integer> tweetBySuburbs() throws IOException;

    Map<?, Integer> largeTweetBySuburbs(String suburb, String date) throws IOException;

    Map<String, List<Object>> getDailyNewCases() throws IOException;

    Map<String, LinkedHashMap<String, Integer>> getHashtags(String year) throws IOException;

    Map<String, Integer> getAllCount() throws IOException;

    Map<String, Map<String, Map<String, Float>>> getAbs(String year) throws IOException;

}
