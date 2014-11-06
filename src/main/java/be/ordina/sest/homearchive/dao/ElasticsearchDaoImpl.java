package be.ordina.sest.homearchive.dao;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import be.ordina.sest.homearchive.helper.ElasticQueryBuilder;
import be.ordina.sest.homearchive.model.RequestResponseDocument;

/**
 *
 * implementation of {@link ElasticsearchDao}
 *
 */
@Repository
@Log4j
public class ElasticsearchDaoImpl implements ElasticsearchDao {
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private Client client;

    //    @PostConstruct
    //    public void setUpRiver() {
    //
    //        try {
    //            XContentBuilder json =
    //                jsonBuilder().startObject().field("type", "mongodb").field("mongodb").startObject()
    //                .field("db", "homearchive_elastic").field("collection", "fs.files").endObject().field("index")
    //                .startObject().field("name", "mongoindex").field("type", "requestresponseentity").endObject()
    //                .endObject();
    //            client.prepareIndex("_river", "mongodb", "_meta").setSource(json).execute();
    //
    //        } catch (IOException e) {
    //            log.error("Error setting river: " + e.getCause());
    //            e.printStackTrace();
    //        }
    //
    //    }

    @Override
    public List<RequestResponseDocument> findDocuments(final RequestResponseDocument document) {
        List<RequestResponseDocument> docList = new ArrayList<RequestResponseDocument>();
        SearchQuery query =
            new ElasticQueryBuilder().addFileName(document.getFilename())
            .addDescription(document.getMetadata().getDescription())
            .addDateRange(document.getStartDate(), document.getEndDate()).buildQuery();
        docList = template.queryForList(query, RequestResponseDocument.class);
        return docList;
    }

}
