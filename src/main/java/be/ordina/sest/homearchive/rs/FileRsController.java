package be.ordina.sest.homearchive.rs;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;
import be.ordina.sest.homearchive.service.FileService;
import be.ordina.sest.homearchive.service.SearchService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Log4j
@RestController
public class FileRsController {

    @Autowired
    private FileService fileService;

    @Autowired
    private SearchService searchService;

    /**
     * Downloads file
     *
     * @param id       String id
     * @param response HttpServletResponse
     * @throws IOException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/fileService/{id}")
    public void downloadFile(@PathVariable("id") final String id, final HttpServletResponse response)
            throws IOException {
        RequestResponseDocument doc = new RequestResponseDocument();
        doc.set_id(id);
        GridFSDBFile dbFile = fileService.downloadFileById(doc);
        String contentType = dbFile.getContentType();
        String fileName = dbFile.getFilename();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        dbFile.writeTo(response.getOutputStream());

    }

    /**
     * Searches for files
     *
     * @param param String
     * @return list of documents
     * @throws ParseException
     * @throws IOException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/fileService")
    public List<RequestResponseDocument> findFiles(@RequestParam(value = "params", required = false) final String param) throws ParseException,
            IOException {
        RequestResponseDocument document = new RequestResponseDocument();
        log.debug("Received parameters: " + param);
        setParams(param, document);
        return searchService.findDocuments(document);
    }

    /**
     * uploads file
     *
     * @param file        MultipartFile
     * @param description String
     * @throws IOException
     */
    @RequestMapping(value = "/fileService", method = RequestMethod.POST)
    public void uploadFile(@RequestParam("file") final MultipartFile file,
                           @RequestParam("description") final String description) throws IOException {
        UploadDocument uploadDocument = new UploadDocument();
        uploadDocument.setFile(file);
        log.debug("Received description: " + description);
        uploadDocument.setDescription(description);
        fileService.uploadFile(uploadDocument);
    }

    /**
     * deletes file
     *
     * @param id String
     * @throws IOException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/fileService/{id}")
    public void deleteDocument(@PathVariable("id") final String id) throws IOException {
        fileService.deleteDocument(id);

    }

    /**
     * updates document metadata
     *
     * @param id       String
     * @param document {@link RequestResponseDocument}
     * @throws ParseException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/fileService/{id}")
    public void updateDocument(@PathVariable("id") final String id,
                               @RequestBody final RequestResponseDocument document) throws ParseException {
        log.debug("Received document" + document);
        log.debug("Updating document with _id:  " + id);
        fileService.updateDocument(id, document);
    }

    /**
     * Stores request parameters in RequestRequestDocument fields
     *
     * @param jsonParams String
     * @param document   {@link RequestResponseDocument}
     * @throws IOException
     */
    private void setParams(final String jsonParams, final RequestResponseDocument document) throws
            IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        if (StringUtils.isNotBlank(jsonParams)) {
            JsonNode params = mapper.readTree(jsonParams);
            if (params.get("fileName") != null && !params.get("fileName").asText().equals("null")) {
                document.setFilename(params.get("fileName").asText());
            }
            if (params.get("startDate") != null) {
                String startDate = params.get("startDate").asText();
                document.setStartDate(parseDate(startDate));
            }
            if (params.get("endDate") != null) {
                String endDate = params.get("endDate").asText();
                document.setEndDate(parseDate(endDate));
            }

            String description = null;
            if (params.get("metadata") != null) {
                JsonNode metadata = params.get("metadata");
                if (metadata.get("description") != null) {
                    description = metadata.get("description").asText();
                }
            }
            document.getMetadata().setDescription(description);
        }
    }

    /**
     * Parses string representation of the date
     *
     * @param date String
     * @return parsedDate
     */
    private Date parseDate(final String date) throws ParseException {
        Date parsedDate = null;
        if (StringUtils.isNotBlank(date) && !date.equals("null")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            parsedDate = df.parse(date.replace('"', ' ').trim());
        }
        return parsedDate;
    }
}
