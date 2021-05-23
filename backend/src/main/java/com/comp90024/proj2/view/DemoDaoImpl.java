package com.comp90024.proj2.view;

import com.comp90024.proj2.entity.Covid;
import com.comp90024.proj2.entity.DemoTweet;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@DependsOn("demoDbConnector")
public class DemoDaoImpl extends CouchDbRepositorySupport<DemoTweet> {

    private static final Logger logger = LoggerFactory.getLogger(DemoDaoImpl.class);

    @Autowired
    public DemoDaoImpl(@Qualifier("demoDbConnector") CouchDbConnector couchdb) {
        super(DemoTweet.class, couchdb);

        try {
            initStandardDesignDocument();
        } catch (NullPointerException e) {
            logger.debug("View DemoDaoImpl is existing.");
        }
    }

    @View(name="suburbs", map="function (keys, values) { return sum(values); }", reduce="function (doc) { emit(doc.suburb, 1); }")
    public Map<String, Integer> getSuburbs() {
        Map<String, Integer> res = new HashMap<>();

        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("suburbs").group(true);
        ViewResult result = db.queryView(query);

        List<ViewResult.Row> bySuburbs = result.getRows();
        for (ViewResult.Row row : bySuburbs) {
            res.put(row.getKey(), Integer.parseInt(row.getValue()));
        }

        return res;

    }
}
