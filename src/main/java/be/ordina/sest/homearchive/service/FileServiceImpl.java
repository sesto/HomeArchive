package be.ordina.sest.homearchive.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
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
 *
 * Implementation of {@link FileService}
 *
 */
@Service
@Log4j
public class FileServiceImpl implements FileService {

    @Autowired
    private MongoDao mongoDao;

    @Override
    public GridFSDBFile downloadFileById(final RequestResponseDocument document) throws IOException {
        String id = document.get_id();
        GridFSDBFile dbFile = mongoDao.findDocumentById(id);
        return dbFile;
    }

    @Override
    public List<RequestResponseDocument> findDocuments(final RequestResponseDocument document) {
        String fileName = document.getFilename();
        String documentType = document.getContentType();
        String description = document.getMetadata().getDescription();
        Date startDate = document.getStartDate();
        Date endDate = document.getEndDate();
        String id = document.get_id();
        Query query =
            new GridFsQueryBuilder().addId(id).addFileName(fileName).addContentType(documentType)
            .addDescription(description).addDateRange(startDate, endDate).getQuery();
        log.info("Starting seacrh with query: " + query);
        log.info("Found documents: " + mongoDao.findDocuments(query));
        return setDocumentFields(mongoDao.findDocuments(query));
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
        mongoDao.deleteDocument(id);
    }

    @Override
    public void updateDocument(final String id, final RequestResponseDocument document) {
        DBObject update = new BasicDBObject();
        update.put("filename", document.getFilename());
        update.put("metadata", new BasicDBObject("description", document.getMetadata().getDescription()));
        log.debug("Update db object: " + update);
        mongoDao.updateDocument(id, new BasicDBObject("$set", update));
    }

    /**
     *
     * Stores file data in the RequestResponseDocument
     *
     * @param fileList
     * @return List<RequestResponseDocument>
     */
    private List<RequestResponseDocument> setDocumentFields(final List<GridFSDBFile> fileList) {
        List<RequestResponseDocument> documents = new ArrayList<RequestResponseDocument>();
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
}
