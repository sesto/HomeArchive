package be.ordina.sest.homearchive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ordina.sest.homearchive.dao.ElasticsearchDao;
import be.ordina.sest.homearchive.model.RequestResponseDocument;

/**
 *
 * Implementation of {@link SearchService}
 *
 */
@Service
public class SearchServiceElasticImpl implements SearchService {

    @Autowired
    private ElasticsearchDao dao;

    @Override
    public List<RequestResponseDocument> findDocuments(final RequestResponseDocument document) {

        return dao.findDocuments(document);
    }

}
