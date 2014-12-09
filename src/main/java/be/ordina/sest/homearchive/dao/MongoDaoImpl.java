package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import com.mongodb.*;
import com.mongodb.gridfs.GridFSDBFile;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * implementation of {@link be.ordina.sest.homearchive.dao.MongoDao}
 *
 * @author sest
 */
@Component
@Log4j
public class MongoDaoImpl implements MongoDao {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private MongoClient mongoClient;

    private final String dbName;

    private DB db;

    @Autowired
    public MongoDaoImpl(@Value("homearchive_elastic") final String dbName) {
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
        gridFsTemplate.store(content, fileName, contentType, metaData);

    }

    @Override
    public GridFSDBFile findDocument(final Query query) {

        return gridFsTemplate.findOne(query);
    }

    @Override
    public List<GridFSDBFile> findDocuments(final Query query) {
        return gridFsTemplate.find(query);
    }

    @Override
    public void deleteDocument(final Query query) {

        gridFsTemplate.delete(query);
    }

    @Override
    public RequestResponseDocument updateDocument(String id, DBObject update) {

        DBCollection collection = getDB().getCollection("fs.files");
        log.debug("Searching " + collection.getFullName() + " for document with id: " + id);
        DBObject query = new BasicDBObject("_id", new ObjectId(id));

        DBObject dbDocument = collection.findAndModify(query, null, null, false,update, true, false);
        log.debug("Modified DB document: " + dbDocument);
        return mapToRequestResponseDocument(dbDocument);
    }

    //    @Override
//    public RequestResponseDocument updateDocument(final String id, final Update update) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("_id").is(id));
//        log.info("Updating document with update query: " + update);
//        RequestResponseDocument requestResponseDocument = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), RequestResponseDocument.class, "fs.files");
//        log.debug("Modified document: " + requestResponseDocument);
//        return requestResponseDocument;
//    }
    private RequestResponseDocument mapToRequestResponseDocument(DBObject dbo) {
        RequestResponseDocument requestResponseDocument = new RequestResponseDocument();
        requestResponseDocument.set_id(dbo.get("_id").toString());
        requestResponseDocument.setFilename(dbo.get("filename").toString());
        requestResponseDocument.getMetadata().setDescription(((DBObject) dbo.get("metadata")).get("description").toString());
        return requestResponseDocument;
    }
}
