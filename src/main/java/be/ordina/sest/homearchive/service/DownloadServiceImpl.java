package be.ordina.sest.homearchive.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import be.ordina.sest.homearchive.dao.MongoDao;
import be.ordina.sest.homearchive.model.DownloadDocument;

import com.mongodb.gridfs.GridFSDBFile;

@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    private MongoDao mongoDao;

    @Override
    public void downloadFileByName(final DownloadDocument downloadDocument) throws IOException {
        String fileName = downloadDocument.getFileName();
        GridFSDBFile dbFile = mongoDao.findDocumentByFileName(fileName);
        System.out.println("found file :" + dbFile.getFilename());
        File dir = new File("TestDirectory");
        boolean created = dir.mkdirs();

        System.out.println(created);
        dbFile.writeTo("TestDirectory/" + dbFile.getFilename());
    }

}
