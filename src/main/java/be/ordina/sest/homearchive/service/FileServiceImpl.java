package be.ordina.sest.homearchive.service;
/**
 * Implementation of {@link be.ordina.sest.homearchive.service.FileService}
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import be.ordina.sest.homearchive.dao.MongoDao;
import be.ordina.sest.homearchive.helper.GridFsQueryBuilder;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * Implementation of {@link FileService}
 */
@Service
@Log4j
public class FileServiceImpl implements FileService {

    @Autowired
    private MongoDao mongoDao;

    @Override
    public GridFSDBFile downloadFileById(final RequestResponseDocument document) throws IOException {
        Query query = getSingleDocumentQuery(document);
        return mongoDao.findDocument(query);
    }

    @Override
    public List<RequestResponseDocument> findDocuments(final RequestResponseDocument document) {
        Query query =
               getMultipleDocumentQuery(document);
        List<GridFSDBFile> docList = mongoDao.findDocuments(query);
        log.info("Starting search with query: " + query);
        log.info("Found documents: " + docList);
        return setDocumentFields(docList);
    }

    @Override
    public void uploadFile(final UploadDocument document) throws IOException {
        MultipartFile file = document.getFile();
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        InputStream content = file.getInputStream();
        DBObject metaData = new BasicDBObject("description", document.getDescription());
        mongoDao.saveDocument(content, fileName, contentType, metaData);
    }

    @Override
    public void deleteDocument(final String id) {
        Query query = new GridFsQueryBuilder().addId(id).getQuery();
        mongoDao.deleteDocument(query);
    }

    @Override
    public void updateDocument(final String id, final RequestResponseDocument document) {
        Update update = getUpdateQuery(document);
        mongoDao.updateDocument(id, update);
    }

    /**
     * Stores file data in the RequestResponseDocument
     *
     * @param fileList list of GridFsFiles
     * @return List<RequestResponseDocument>
     */
    protected List<RequestResponseDocument> setDocumentFields(final List<GridFSDBFile> fileList) {
        List<RequestResponseDocument> documents = new ArrayList<>();
        for (GridFSDBFile gridFSDBFile : fileList) {
            RequestResponseDocument document1 = new RequestResponseDocument();
            document1.setFilename(gridFSDBFile.getFilename());
            document1.set_id((gridFSDBFile.getId().toString()));
            document1.setContentType(gridFSDBFile.getContentType());
            String description = (String) gridFSDBFile.getMetaData().get("description");
            document1.getMetadata().setDescription(description);
            document1.setUploadDate((gridFSDBFile.getUploadDate()));
            documents.add(document1);
        }
        return documents;
    }

    /**
     * builds query to search multiple documents
     * @param document {@link be.ordina.sest.homearchive.model.RequestResponseDocument}
     * @return query
     */
    protected Query getMultipleDocumentQuery(final RequestResponseDocument document) {
        String fileName = document.getFilename();
        String documentType = document.getContentType();
        String description = document.getMetadata().getDescription();
        Date startDate = document.getStartDate();
        Date endDate = document.getEndDate();
        String id = document.get_id();
        return new GridFsQueryBuilder().addId(id).addFileName(fileName).addContentType(documentType)
                .addDescription(description).addDateRange(startDate, endDate).getQuery();
    }

    /**
     * builds query to return single document
     * @param document {@link RequestResponseDocument}
     * @return query
     */
    protected Query getSingleDocumentQuery(final RequestResponseDocument document){
        String id = document.get_id();
        return new GridFsQueryBuilder().addId(id).getQuery();
    }

    /**
     * returns query for updating document
     * @param requestResponseDocument {@link RequestResponseDocument}
     * @return update
     */

    protected Update getUpdateQuery(final  RequestResponseDocument requestResponseDocument){
        Update update = new Update();
        update.set("filename", requestResponseDocument.getFilename());
        update.set("metadata", new BasicDBObject("description", requestResponseDocument.getMetadata().getDescription()));
        return update;
    }
}
