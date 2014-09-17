package be.ordina.sest.homearchive.dao;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

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


    GridFSDBFile findDocumentByFileName(final String fileName);

}