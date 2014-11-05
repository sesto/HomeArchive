package be.ordina.sest.homearchive.dao;

import java.util.List;

import be.ordina.sest.homearchive.model.RequestResponseDocument;

/**
 *
 * Dao for elasticsearch
 *
 */
public interface ElasticsearchDao {

    /**
     *
     * searches for documents using elasticsearch
     *
     * @param document
     * @return List<RequestResponseDocument>
     */
    List<RequestResponseDocument> findDocuments(final RequestResponseDocument document);
}
