package be.ordina.sest.homearchive.dao;

import java.util.List;

import be.ordina.sest.homearchive.model.RequestResponseDocument;

public interface ElasticsearchDao {


    List<RequestResponseDocument> findDocuments(final RequestResponseDocument document);
}
