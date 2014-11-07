package be.ordina.sest.homearchive.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import be.ordina.sest.homearchive.helper.ElasticQueryBuilder;
import be.ordina.sest.homearchive.helper.JSONHelper;
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

    @PostConstruct
    public void setUpRiver() throws IOException {

        // InputStream res = this.getClass().getResourceAsStream("mapping.json");
        // log.debug("The stream: " + res);
        // byte[] bytes = IOUtils.toByteArray(res);
        //
        ClusterState clusterState =
            client.admin().cluster().prepareState().setIndices("mongoindex", "_river").execute().actionGet()
            .getState();
        IndexMetaData mongoindexMetadata = clusterState.getMetaData().index("mongoindex");
        if (mongoindexMetadata == null) {
            log.info("Adding elasticsearch settings: " + JSONHelper.getSettings());
            log.info("Adding mappings: " + JSONHelper.getMapping());
            client.admin().indices().prepareCreate("mongoindex").setSource(JSONHelper.getSettings())
            .addMapping("requestresponseentity", JSONHelper.getMapping()).execute();
        } else {
            log.info("Mongoindex Mappings already exist. Not adding any mappings");
        }
        IndexMetaData riverMetadata = clusterState.getMetaData().index("_river");
        if (riverMetadata == null) {
            log.info("Adding river configuration: " + JSONHelper.getRiverSettings());
            client.prepareIndex("_river", "mongodb", "_meta").setSource(JSONHelper.getRiverSettings()).execute();
        } else {
            log.info("River Mappings already exist. Not adding any mappings ");
        }
    }

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
