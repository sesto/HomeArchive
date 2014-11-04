package be.ordina.sest.homearchive.dao;

import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import be.ordina.sest.homearchive.helper.ElasticQueryBuilder;
import be.ordina.sest.homearchive.model.RequestResponseDocument;

@Repository
@Log4j
public class ElasticsearchDaoImpl implements ElasticsearchDao {
    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    private Client client;

    @Override
    public List<RequestResponseDocument> findAll() {
        log.info("Mapping for documents:  " + template.getMapping(RequestResponseDocument.class));
        log.info("Index exists: " + template.indexExists(RequestResponseDocument.class));
        SearchQuery query = new NativeSearchQueryBuilder().build();
        log.info("Query for the document: " + query.toString());
        log.info("Documents count: " + template.count(query, RequestResponseDocument.class));
        GetResponse response =
            client.prepareGet("mongoindex", "requestresponseentity", "54573e153004114b4935a9a5").execute()
            .actionGet();
        log.debug("Found doc with client: " + response.getFields());
        log.debug("Response headers with client: " + response.getHeaders());
        return template.queryForList(query, RequestResponseDocument.class);
    }

    @Override
    public List<RequestResponseDocument> findDocuments(final RequestResponseDocument document) {
        List<RequestResponseDocument> docList = new ArrayList<RequestResponseDocument>();
        SearchQuery query =
            new ElasticQueryBuilder().addFileName(document.getFilename())
            .addDescription(document.getMetadata().getDescription()).buildQuery();
        docList = template.queryForList(query,  RequestResponseDocument.class);
        return docList;
    }

}
