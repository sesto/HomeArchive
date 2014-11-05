package be.ordina.sest.homearchive.service;

import java.io.IOException;
import java.util.List;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;

import com.mongodb.gridfs.GridFSDBFile;

public interface FileService {
    /**
     * Downloads file by id TODO
     *
     * @param downloadDocument
     * @return
     * @throws IOException
     */
    GridFSDBFile downloadFileById(final RequestResponseDocument downloadDocument) throws IOException;
    /**
     *
     * Searches for documents in db
     *
     * @param document
     * @return {@link RequestResponseDocument}
     */

    List<RequestResponseDocument> findDocuments(final RequestResponseDocument document);

    /**
     *
     * Uploads file
     *
     * @param document {@link UploadDocument}
     * @throws IOException
     */
    void uploadFile(final UploadDocument document) throws IOException;
    /**
     *
     * Uploads file
     *
     * @param id
     */

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