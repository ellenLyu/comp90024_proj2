/*
 * COMP90024: Cluster and Cloud Computing – Assignment 2
 * 2021 semester 1
 * Team 27
 * City Analytics om the Cloud
 */

package com.comp90024.proj2.service;

import java.io.IOException;

public interface PythonService {

  void crawlCovid() throws IOException, InterruptedException;

  void crawlTweetGeo() throws IOException, InterruptedException;
}
