package be.ordina.sest.homearchive.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

@Service
@Log4j
public class FileServiceImpl implements FileService {

    @Autowired
    private MongoDao mongoDao;

    @Override
    public GridFSDBFile downloadFileById(final RequestResponseDocument document) throws IOException {
        String id = document.getId();
        GridFSDBFile dbFile = mongoDao.findDocumentById(id);
        return dbFile;
    }

    @Override
    public List<GridFSDBFile> findDocuments(final RequestResponseDocument document) {
        String fileName = document.getFileName();
        String documentType = document.getDocumentType();
        List<String> tags = document.getTags();
        Date startDate = document.getStartDate();
        Date endDate = document.getEndDate();
        String id = document.getId();
        Query query =
            new GridFsQueryBuilder().addId(id).addFileName(fileName).addContentType(documentType).addTags(tags)
            .addDateRange(startDate, endDate).getQuery();
        log.info("Starting seacrh with query: " + query);
        log.info("Found documents: " + mongoDao.findDocuments(query));
        return mongoDao.findDocuments(query);
    }

    @Override
    public GridFSDBFile findDocumentById(final RequestResponseDocument document) {
        String id = document.getId();
        GridFSDBFile dbFile = mongoDao.findDocumentById(id);
        log.info("Found file: " + dbFile);
        return dbFile;
    }

    @Override
    public void uploadFile(final UploadDocument document) throws IOException {
        MultipartFile file = document.getFile();
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        InputStream content = file.getInputStream();
        DBObject metaData = new BasicDBObject("tags", document.getTags());
        mongoDao.saveDocument(content, fileName, contentType, metaData);
    }

    @Override
    public void deleteDocument(final String id) {
        mongoDao.deleteDocument(id);
    }

    @Override
    public void updateDocument(final String id, final RequestResponseDocument document) {
        DBObject update = new BasicDBObject();
        update.put("filename", document.getFileName());
        update.put("metadata", new BasicDBObject("tags", document.getTags()));
        log.debug("Update db object: " + update);
        mongoDao.updateDocument(id, new BasicDBObject("$set", update));
    }

}
