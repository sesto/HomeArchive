package be.ordina.sest.homearchive.service;

import be.ordina.sest.homearchive.dao.ElasticsearchDao;
import be.ordina.sest.homearchive.helper.ElasticQueryBuilder;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link SearchService}
 */
@Service
public class SearchServiceElasticImpl implements SearchService {

    @Autowired
    private ElasticsearchDao dao;

    @Override
    public List<RequestResponseDocument> findDocuments(final RequestResponseDocument document) {
        SearchQuery query =
                new ElasticQueryBuilder().addFileName(document.getFilename())
                        .addDescription(document.getMetadata().getDescription())
                        .addDateRange(document.getStartDate(), document.getEndDate()).buildQuery();
        return dao.findDocuments(query);
    }

}
