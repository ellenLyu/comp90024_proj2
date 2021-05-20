package com.comp90024.proj2.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface SearchService {

    List<List<Float>> groupByDate(String date) throws IOException;

    List<List<Float>> getAll() throws IOException;

    List<Map<String, Object>> tweetBySentiment() throws IOException;

    Map<String, Integer> tweetBySuburbs() throws IOException;

    Map<? extends Object, Integer> largeTweetBySuburbs(String suburb, String date) throws IOException;

}
