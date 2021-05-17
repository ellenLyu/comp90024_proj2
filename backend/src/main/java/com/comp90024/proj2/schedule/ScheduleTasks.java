package com.comp90024.proj2.schedule;

import java.io.IOException;
import java.util.Timer;
import com.comp90024.proj2.service.impl.PythonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasks {

  @Autowired
  private PythonServiceImpl pythonService;

  public ScheduleTasks() {

    cleanLogs();

  }

//  @Scheduled(fixedDelay = 1000)
  @Scheduled(cron = "0 20 23 * * ?", zone = "GMT+8:00")
  private void cleanLogs() {
    try {
      pythonService.crawlCovid();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

}
