package be.ordina.sest.homearchive.dao;

import java.util.ArrayList;
import java.util.List;

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
public class ElasticsearchDaoImpl implements ElasticsearchDao {
    @Autowired
    private ElasticsearchTemplate template;

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
