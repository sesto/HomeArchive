package be.ordina.sest.homearchive.dao;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import be.ordina.sest.homearchive.helper.GridFsQueryBuilder;

import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * Dao for CRUD MongoDB operations
 *
 * @author sest
 *
 */
@Component
public class MongoDaoImpl implements MongoDao {
    @Autowired
    private GridFsTemplate template;

    @Override
    public void saveDocument(final InputStream content, final String fileName, final String contentType,
        final DBObject metaData) throws FileNotFoundException {
        template.store(content, fileName, contentType, metaData);

    }

    @Override
    public GridFSDBFile findDocumentById(final String id) {
        Query query = new GridFsQueryBuilder().addId(id).getQuery();
        return template.findOne(query);
    }

    @Override
    public List<GridFSDBFile> findDocuments(final Query query) {
        return template.find(query);
    }



}
