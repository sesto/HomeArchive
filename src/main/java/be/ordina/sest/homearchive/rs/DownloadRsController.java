package be.ordina.sest.homearchive.rs;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ordina.sest.homearchive.model.RequestDocument;
import be.ordina.sest.homearchive.service.DownloadService;

import com.mongodb.gridfs.GridFSDBFile;

@RestController
public class DownloadRsController {

    @Autowired
    private DownloadService service;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/download/{filename}", produces = {
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/xml", "image/jpeg",
    "application/pdf"})
    public FileSystemResource getDocument(@PathVariable("filename") final String fileName) throws IOException {
        RequestDocument doc = new RequestDocument();
        String correctName = fileName.replace('_', '.');
        System.out.println("Requested file name is :  " + correctName);
        doc.setFileName(correctName);
        GridFSDBFile dbFile = service.downloadFileByName(doc);
        File outputFile = new File("outputFile");
        dbFile.writeTo(outputFile);
        return new FileSystemResource(outputFile);
    }
}
