package com.comp90024.proj2.view;

import com.comp90024.proj2.entity.Covid;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn("covidDbConnector")
public class CovidDaoImpl extends CouchDbRepositorySupport<Covid> {

    private CouchDbConnector covidDbConnector;

    @Autowired
    public CovidDaoImpl(@Qualifier("covidDbConnector") CouchDbConnector couchdb) {
        super(Covid.class, couchdb);

        try {
            initStandardDesignDocument();
        } catch (NullPointerException e) {
            System.out.println("exists");
        }
    }

    @View(name = "by_data_date", map = "function(doc) { if (doc.data_date) { emit(doc.data_date, doc) } }")
//    @GenerateView
    public List<Covid> findBydata_date(String date) {
//        ViewQuery query = createQuery("by_data_date").key(date).descending(true);
//        return db.queryView(query, Covid.class);
        return queryView("by_data_date", date);
    }


}
