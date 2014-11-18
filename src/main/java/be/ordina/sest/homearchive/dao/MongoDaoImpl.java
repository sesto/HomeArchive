package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Dao for CRUD MongoDB operations
 *
 * @author sest
 */
@Component
@Log4j
public class MongoDaoImpl implements MongoDao {
    @Autowired
    private GridFsTemplate gridFsTemlate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void saveDocument(final InputStream content, final String fileName, final String contentType,
                             final DBObject metaData) throws FileNotFoundException {
        gridFsTemlate.store(content, fileName, contentType, metaData);

    }

    @Override
    public GridFSDBFile findDocument(final Query query) {

        return gridFsTemlate.findOne(query);
    }

    @Override
    public List<GridFSDBFile> findDocuments(final Query query) {
        return gridFsTemlate.find(query);
    }

    @Override
    public void deleteDocument(final Query query) {

        gridFsTemlate.delete(query);
    }

    @Override
    public void updateDocument(final String id, final Update update) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        log.info("Updating document with update query: " + update);
        RequestResponseDocument requestResponseDocument = mongoTemplate.findAndModify(query, update, RequestResponseDocument.class, "fs.files");
        log.debug("Found document to modify: " + requestResponseDocument);
    }

}
