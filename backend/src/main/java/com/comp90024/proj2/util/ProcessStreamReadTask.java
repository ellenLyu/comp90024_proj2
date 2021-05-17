package com.comp90024.proj2.util;

public class ProcessStreamReadTask {private volatile String message = "";

  private volatile String error = "";

  private Thread readMessage;

  private Thread readError;

  public ProcessStreamReadTask(Process process) {
    this.readMessage = new Thread(() -> message = convertToStr(process.getInputStream()));
    this.readError = new Thread(() -> error = convertToStr(process.getErrorStream()));
  }

  public void start() {
    this.readMessage.start();
    this.readError.start();
  }

  public void waitForRead() {
    try {
      this.readMessage.join();
      this.readError.join();;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public String getMessage() {
    return message;
  }

  public String getError() {
    return error;
  }


  /**
   * 将流转换成字符串
   *
   * @param inputStream
   * @return
   */
  private String convertToStr(InputStream inputStream) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder sb = new StringBuilder();

    try {
      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        inputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return sb.toString();
  }
}

  /**
   * 执行python文件, 文件执行失败会抛出运行时异常
   *
   * @param args 执行参数
   * @return 标准输出流信息
   */
  private String exec(String... args) {

    int exitStatus = -1;
    ProcessStreamReadTask readTask = null;
    Process process = null;
    try {
      ProcessBuilder builder = build(PY_API, args);
      process = builder.start();

      readTask = new ProcessStreamReadTask(process);
      readTask.start();
      exitStatus = process.waitFor();
      readTask.waitForRead();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
      process.destroy();
    }
    // 将标准错误流封装成异常跑出
    if (exitStatus != 0) {
      String error = readTask.getError();
      throw new RuntimeException(error);
    } else {
      // 读取标准输出流并返回
      System.out.println(readTask.getMessage());
      return readTask.getMessage();
    }
  }

}
