package java.com.comp90024.proj2.service.impl;

import java.util.Timer;
import com.comp90024.proj2.service.impl.PythonServiceImpl;

@Component
public class ScheduleTasks {

  @Autowired
  private PythonServiceImpl pythonService;

  public ScheduleTasks() {

    cleanLogs();

  }

  @Scheduled(fixedDelay = 60 * 1000)
//  @Scheduled(cron = "0 0 1 * * ?")
  private void cleanLogs() {
    System.out.println("scheduled task");
  }

}
