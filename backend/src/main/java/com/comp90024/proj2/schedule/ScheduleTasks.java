package com.comp90024.proj2.schedule;

import java.io.IOException;
import java.util.Timer;

import com.comp90024.proj2.service.impl.PythonServiceImpl;
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

//  public ScheduleTasks() {
//    cleanLogs();
//  }

//  @Scheduled(fixedDelay = 60 * 1000)
  @Scheduled(cron = "0 45 13 * * ?", zone = "GMT+8:00")
  private void crawlCovidCases() {

    logger.info("Scheduled task crawlCovidCases is called");
    try {
      pythonService.crawlCovid();
    } catch (IOException | InterruptedException | NullPointerException e) {
      e.printStackTrace();
    }
  }

}