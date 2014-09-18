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
    public GridFSDBFile downloadFileByName(final RequestDocument document) throws IOException {
        String fileName = document.getFileName();
        GridFSDBFile dbFile = mongoDao.findDocumentByFileName(fileName);
        System.out.println("found file :" + dbFile.getFilename());
        File dir = new File("TestDirectory");
        boolean created = dir.mkdirs();

        System.out.println(created);
        dbFile.writeTo("TestDirectory/" + dbFile.getFilename());
        return dbFile;
    }

    @Override
    public List<GridFSDBFile> findDocuments(final RequestDocument document) {
        String fileName = document.getFileName();
        String documentType = document.getDocumentType();
        List<String> tags = document.getTags();
        Date startDate = document.getStartDate();
        Date endDate = document.getEndDate();
        Query query =
            new GridFsQueryBuilder().addFileName(fileName).addContentType(documentType).addTags(tags)
            .addDateRange(startDate, endDate).getQuery();
        log.info("Starting seacrh with query: " + query);
        mongoDao.findDocuments(query);
        return mongoDao.findDocuments(query);
    }

}
