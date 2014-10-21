package be.ordina.sest.homearchive.dao;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import be.ordina.sest.homearchive.helper.GridFsQueryBuilder;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * Dao for CRUD MongoDB operations
 *
 * @author sest
 *
 */
@Component
@Log4j
public class MongoDaoImpl implements MongoDao {
    @Autowired
    private GridFsTemplate gridFsTemlate;

    private final String dbName;

    private MongoClient mongoClient;

    private DB db;

    @Autowired
    public MongoDaoImpl(@Value("homearchive") final String dbName) {
        this.dbName = dbName;
    }

    /**
     * Get mongoClient
     *
     * @return mongoClient
     */
    @SneakyThrows
    public MongoClient getClient() {
        if (mongoClient == null) {
            log.trace("Opening mongoClient");
            mongoClient = new MongoClient("localhost", 27017);
        }
        return mongoClient;
    }

    /**
     * Get Mongo db
     *
     * @return DB
     */
    public DB getDB() {
        if (db == null) {
            if (log.isTraceEnabled()) {
                log.trace("Loading DB " + dbName);
            }
            db = getClient().getDB(dbName);
        }
        return db;
    }

    @Override
    public void saveDocument(final InputStream content, final String fileName, final String contentType,
        final DBObject metaData) throws FileNotFoundException {
        gridFsTemlate.store(content, fileName, contentType, metaData);

    }

    @Override
    public GridFSDBFile findDocumentById(final String id) {
        Query query = new GridFsQueryBuilder().addId(id).getQuery();
        return gridFsTemlate.findOne(query);
    }

    @Override
    public List<GridFSDBFile> findDocuments(final Query query) {
        return gridFsTemlate.find(query);
    }

    @Override
    public void deleteDocument(final String id) {
        Query query = new GridFsQueryBuilder().addId(id).getQuery();
        gridFsTemlate.delete(query);
    }

    @Override
    public void updateDocument(final String id, final DBObject update) {
        DBCollection collection = getDB().getCollection("fs.files");
        log.debug("Searching " + collection.getFullName() + " for document with id: " + id);
        DBObject query = new BasicDBObject("_id", new ObjectId(id));

        DBObject dbDocument = collection.findAndModify(query, update);
        log.debug("Found document to modify: " + dbDocument);
    }

}
