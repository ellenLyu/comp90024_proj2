/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.schedule;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;

import com.comp90024.proj2.controller.SearchController;
import com.comp90024.proj2.service.impl.PythonServiceImpl;
import com.comp90024.proj2.service.impl.SearchServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

  private static final Logger logger = LoggerFactory.getLogger(ScheduleTasks.class);

  @Autowired
  private PythonServiceImpl pythonService;

  @Autowired
  private SearchServiceImpl searchService;

//  public ScheduleTasks() {
//    cleanLogs();
//  }

//   @Scheduled(fixedDelay = 60 * 1000)
  @Scheduled(cron = "0 55 14 * * ?", zone = "GMT+8:00")
  private void crawlCovidCases() {

    logger.info("Scheduled task Crawl CovidCases is called");
    try {
      pythonService.crawlCovid();
    } catch (IOException | InterruptedException | NullPointerException e) {
      e.printStackTrace();
    }
  }

//  @Scheduled(fixedDelay = 24 * 60 * 1000)
  @Scheduled(cron = "0 0 10 * * ?", zone = "GMT+8:00")
  private void crawlTweetSuburb() {

    logger.info("Scheduled task Crawl TweetSuburb is called");
    try {
      pythonService.crawlTweetGeo();
      clearCache();
    } catch (IOException | InterruptedException | NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Scheduled(fixedDelay = 8 * 60 * 60 * 1000)
  private void clearCache() {
    searchService.clearCache();

    Calendar date = Calendar.getInstance();
    int year = Integer.parseInt(String.valueOf(date.get(Calendar.YEAR)));
    for (int y = 2014; y <= year; y++) {
      logger.info("Update Cache 'hashtags for " + y);
      searchService.getHashtags(String.valueOf(y));
    }
  }
}
