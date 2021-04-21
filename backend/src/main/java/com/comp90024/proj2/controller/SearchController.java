package com.comp90024.proj2.controller;

import com.comp90024.proj2.service.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    public SearchServiceImpl searchService;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public void testAdd() {
        searchService.createDoc();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public void testGet() {
        searchService.createDoc();
    }




}
