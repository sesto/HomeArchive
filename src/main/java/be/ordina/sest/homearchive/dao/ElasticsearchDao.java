package be.ordina.sest.homearchive.dao;

import java.util.List;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 *
 * Dao for elasticsearch
 * @author sest
 */
public interface ElasticsearchDao {

    /**
     *
     * searches for documents using elasticsearch
     *
     * @param query search query
     * @return List<RequestResponseDocument>
     */
    List<RequestResponseDocument> findDocuments(final SearchQuery query);
}
