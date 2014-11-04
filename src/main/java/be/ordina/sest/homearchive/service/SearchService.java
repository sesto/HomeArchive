package be.ordina.sest.homearchive.service;

import java.util.List;

import be.ordina.sest.homearchive.model.RequestResponseDocument;

public interface SearchService {
    List<RequestResponseDocument> findDocuments(final RequestResponseDocument document);
}
