package be.ordina.sest.homearchive.service;
/**
 * Elasticsearch service
 */
import java.util.List;

import be.ordina.sest.homearchive.model.RequestResponseDocument;

/**
 * Service to search files with elastic search
 *
 *
 */
public interface SearchService {
    /**
     *
     * Returns results of the elastic search
     *
     * @param document query document {@link be.ordina.sest.homearchive.model.RequestResponseDocument}
     * @return List<RequestResponseDocument>
     */
    List<RequestResponseDocument> findDocuments(final RequestResponseDocument document);
}
