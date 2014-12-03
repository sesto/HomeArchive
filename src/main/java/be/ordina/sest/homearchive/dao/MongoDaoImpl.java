package be.ordina.sest.homearchive.dao;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
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
    public RequestResponseDocument updateDocument(final String id, final Update update) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        log.info("Updating document with update query: " + update);
        RequestResponseDocument requestResponseDocument = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true),RequestResponseDocument.class, "fs.files");
        log.debug("Modified document: " + requestResponseDocument);
        return requestResponseDocument;
    }

}
