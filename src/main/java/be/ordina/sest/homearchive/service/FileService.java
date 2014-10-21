package be.ordina.sest.homearchive.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;

import com.mongodb.gridfs.GridFSDBFile;

public interface FileService {

    GridFSDBFile downloadFileById(final RequestResponseDocument downloadDocument) throws IOException;

    List<GridFSDBFile> findDocuments(final RequestResponseDocument document);

    GridFSDBFile findDocumentById(final RequestResponseDocument documents);

    void uploadFile(final UploadDocument document) throws IOException;

    public void deleteDocument(final String id);

    /**
     *
     * udates metadata
     *
     * @param id
     * @param document
     */
    void updateDocument(final String id, final RequestResponseDocument document);

}