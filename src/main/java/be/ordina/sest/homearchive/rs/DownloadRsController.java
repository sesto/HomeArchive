package be.ordina.sest.homearchive.rs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ordina.sest.homearchive.model.RequestDocument;
import be.ordina.sest.homearchive.service.DownloadService;

import com.mongodb.gridfs.GridFSDBFile;

@Log4j
@RestController
public class DownloadRsController {

    @Autowired
    private DownloadService service;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/findFiles/{id}")
    public/* FileSystemResource */void getDocument(@PathVariable("id") final String id,
        final HttpServletResponse response) throws IOException {
        RequestDocument doc = new RequestDocument();
        doc.setId(id);
        GridFSDBFile dbFile = service.downloadFileById(doc);
        String contentType = dbFile.getContentType();
        String fileName = dbFile.getFilename();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        dbFile.writeTo(response.getOutputStream());
        // return new FileSystemResource(outputFile);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/findFiles")
    public List<RequestDocument> findFiles(@RequestParam(value = "fileName", required = false) final String fileName) {
        RequestDocument document = new RequestDocument();

        document.setFileName(fileName);
        List<RequestDocument> documents = new ArrayList<RequestDocument>();
        List<GridFSDBFile> files = service.findDocuments(document);
        for (GridFSDBFile gridFSDBFile : files) {
            RequestDocument document1 = new RequestDocument();
            document1.setFileName(gridFSDBFile.getFilename());
            document1.setId((gridFSDBFile.getId().toString()));
            document1.setDocumentType(gridFSDBFile.getContentType());
            Object tags = gridFSDBFile.getMetaData().get("tags");
            @SuppressWarnings("unchecked")
            List<String> tagList = (List<String>) tags;
            document1.setTags(tagList);
            document1.setDocDate((gridFSDBFile.getUploadDate()));
            documents.add(document1);

        }
        return documents;
    }

}
