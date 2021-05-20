package com.comp90024.proj2.view;

import com.comp90024.proj2.entity.Tweet;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
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
@DependsOn("tweetDbConnector")
public class TweetDaoImpl extends CouchDbRepositorySupport<Tweet> {

    private static final Logger logger = LoggerFactory.getLogger(TweetDaoImpl.class);

    @Autowired
    public TweetDaoImpl(@Qualifier("tweetDbConnector") CouchDbConnector couchdb) {
        super(Tweet.class, couchdb);
        try {
            initStandardDesignDocument();
        } catch (NullPointerException e) {
            logger.debug("View TweetDaoImpl is existing.");
        }
    }

//
//    @View( name="count_by_sentiment", map = "function(doc) { if(doc.sentiment) {emit(doc.sentiment, doc._id)} }")
////    @GenerateView
//    public Map<String, Integer> findBySentiment() {
//        Map<String, Integer> res = new HashMap<>();
//
//        ViewQuery query = new ViewQuery().designDocId("_design/example").viewName("count_by_sentiment");
//        ViewResult result = db.queryView(query);
//        res.put("total", Integer.parseInt(result.getRows().get(0).getValue()));
//
//        query = new ViewQuery().designDocId("_design/example").viewName("count_by_sentiment").group(true);
////        ViewQuery query = createQuery("count_by_sentiment").group(true);
//        result = db.queryView(query);
//        List<ViewResult.Row> bySentiment = result.getRows();
//        for (ViewResult.Row row : bySentiment) {
//            res.put(row.getKey(), Integer.parseInt(row.getValue()));
//        }

//        return res;
//
//    }


}
