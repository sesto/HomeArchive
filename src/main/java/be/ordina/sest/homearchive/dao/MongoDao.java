package be.ordina.sest.homearchive.dao;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import be.ordina.sest.homearchive.model.RequestResponseDocument;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

public interface MongoDao {
    /**
     * Saves file in MongoDB
     *
     * @param content
     * @param contenType
     * @param fileName
     * @param metaData
     * @throws FileNotFoundException
     */
    void saveDocument(final InputStream content, final String fileName, final String contentType,
        final DBObject metaData) throws FileNotFoundException;

    /**
     *
     * returns one document
     *
     * @param fileName
     * @return document
     */

    GridFSDBFile findDocumentById(final String fileName);

    /**
     *
     * returns list of documents based on query
     *
     * @param query
     * @return list of documents
     */

    List<GridFSDBFile> findDocuments(final Query query);

    void deleteDocument(final String id);

    /**
     * updates metadata
     *
     *
     * @param id
     * @param data
     */
    void updateDocument(final String id, final DBObject data);

}