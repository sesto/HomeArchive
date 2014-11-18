package be.ordina.sest.homearchive.service;
/**
 * Service downloads, uploads and modifies files
 */
import java.io.IOException;
import java.util.List;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;

import com.mongodb.gridfs.GridFSDBFile;

public interface FileService {
    /**
     * Downloads file by id
     *
     * @param downloadDocument query document {@link be.ordina.sest.homearchive.model.RequestResponseDocument}
     * @return GridFSDBFile
     * @throws IOException
     */
    GridFSDBFile downloadFileById(final RequestResponseDocument downloadDocument) throws IOException;
    /**
     *
     * Searches for documents in db
     *
     * @param document query document {@link be.ordina.sest.homearchive.model.RequestResponseDocument}
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
     * @param id id of the document
     */

    public void deleteDocument(final String id);

    /**
     *
     * udates metadata
     *
     * @param id id of the document
     * @param document {@link be.ordina.sest.homearchive.model.RequestResponseDocument}
     */
    void updateDocument(final String id, final RequestResponseDocument document);

}