package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Dao for CRUD MongoDB operations
 * @author sest
 */
public interface MongoDao {
    /**
     * Saves file in MongoDB
     *
     * @param content binary content
     * @param contentType MIME content type
     * @param fileName name of the file
     * @param metaData meta data
     * @throws FileNotFoundException
     */
    void saveDocument(final InputStream content, final String fileName, final String contentType,
        final DBObject metaData) throws FileNotFoundException;

    /**
     *
     * returns one document
     *
     * @param query search query
     * @return GridFSDBFile
     */

    GridFSDBFile findDocument(final Query query);

    /**
     *
     * returns list of documents based on query
     *
     * @param query search query
     * @return list of documents
     */

    List<GridFSDBFile> findDocuments(final Query query);

    /**
     * Deletes document
     * @param query search query
     */
    void deleteDocument(final Query query);

    /**
     * updates metadata
     *
     *
     * @param id document _id
     * @param update update query
     */
  //  RequestResponseDocument updateDocument(final String id, final Update update);

    RequestResponseDocument updateDocument(final String id, final DBObject update);
}