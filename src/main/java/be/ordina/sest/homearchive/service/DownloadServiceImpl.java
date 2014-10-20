package be.ordina.sest.homearchive.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import be.ordina.sest.homearchive.dao.MongoDao;
import be.ordina.sest.homearchive.helper.GridFsQueryBuilder;
import be.ordina.sest.homearchive.model.RequestDocument;

import com.mongodb.gridfs.GridFSDBFile;

@Service
@Log4j
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    private MongoDao mongoDao;

    @Override
    public GridFSDBFile downloadFileById(final RequestDocument document) throws IOException {
        String id = document.getId();
        GridFSDBFile dbFile = mongoDao.findDocumentById(id);
        return dbFile;
    }

    @Override
    public List<GridFSDBFile> findDocuments(final RequestDocument document) {
        String fileName = document.getFileName();
        String documentType = document.getDocumentType();
        List<String> tags = document.getTags();
        Date startDate = document.getStartDate();
        Date endDate = document.getEndDate();
        String id = document.getId();
        Query query =
            new GridFsQueryBuilder()
        .addId(id)
        .addFileName(fileName)
        .addContentType(documentType)
        .addTags(tags)
        .addDateRange(startDate, endDate).getQuery();
        log.info("Starting seacrh with query: " + query);
        log.info("Found documents: " + mongoDao.findDocuments(query));
        return mongoDao.findDocuments(query);
    }

    @Override
    public GridFSDBFile findDocumentById(final RequestDocument document) {
        String id = document.getId();
        GridFSDBFile dbFile = mongoDao.findDocumentById(id);
        log.info("Found file: " + dbFile);
        return dbFile;
    }

}
