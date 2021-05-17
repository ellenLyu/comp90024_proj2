package com.comp90024.proj2.service.impl;

@Service
public class PythonServiceImpl {

  @Value("${file.python.path}")
  String PY_PATH;

  private static final String PY_API = "API.pyc";


  @Override
  public void crawlCovid() {
    exec("test.py");
  }

  private ProcessBuilder build(String pyFile, String[] args) throws IOException {
    // Get the class path
    File directory = new File(PY_PATH);

    List<String> command = new ArrayList<>();
    command.add("python3");
    command.add("test.py");
    if (args != null && args.length > 0) {
      command.addAll(Arrays.asList(args));
    }

    ProcessBuilder builder = new ProcessBuilder(command);
    // Convert the directory to python.
    builder.directory(directory);
    return builder;
  }

}
