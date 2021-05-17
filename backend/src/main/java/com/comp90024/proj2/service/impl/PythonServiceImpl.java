package com.comp90024.proj2.service.impl;

import com.comp90024.proj2.service.PythonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PythonServiceImpl implements PythonService {

    private static final Logger logger = LoggerFactory.getLogger(PythonServiceImpl.class);

    @Value("${python.py.path}")
    private String PY_PATH;

    @Override
    public void crawlCovid() throws IOException, InterruptedException {
        String pyFile = PY_PATH + "crawl_covid_cases.py";
        exec(pyFile);
    }

    /**
     *
     * @param args
     * @return
     */
    private String exec(String pyFile, String... args) {

        int exitStatus = -1;
        ProcessStreamReadTask readTask = null;
        Process process = null;

        try {
            ProcessBuilder builder = build(pyFile, args);
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
        logger.info("Python command finished");

        if (exitStatus != 0) {
            String error = readTask.getError();
            logger.error(error);
            throw new RuntimeException(error);
        } else {
            return readTask.getMessage();
        }
    }

    private ProcessBuilder build(String pyFile, String[] args) throws IOException {

        File directory = new File(PY_PATH);

        logger.info("Python file location: " + directory.getAbsolutePath());

        List<String> command = new ArrayList<>();
        command.add("python3");
        command.add(pyFile);
        if (args != null && args.length > 0) {
            command.addAll(Arrays.asList(args));
        }
        logger.info("Python Command: {}", command);
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(directory);
        return builder;
    }

    private class ProcessStreamReadTask {

        private volatile String message = "";

        private volatile String error = "";

        private final Thread readMessage;

        private final Thread readError;

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
                this.readError.join();
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
         * Convert InputStream to String
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
                    sb.append(line).append("\n");
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


}
