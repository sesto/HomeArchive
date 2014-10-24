package be.ordina.sest.homearchive.rs;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;
import be.ordina.sest.homearchive.service.FileService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;

@Log4j
@RestController
public class FileRsController {

    @Autowired
    private FileService service;

    /**
     *
     * Downloads file
     *
     * @param id
     * @param response
     * @throws IOException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/findFiles/{id}")
    public void downloadFile(@PathVariable("id") final String id, final HttpServletResponse response)
        throws IOException {
        RequestResponseDocument doc = new RequestResponseDocument();
        doc.setId(id);
        GridFSDBFile dbFile = service.downloadFileById(doc);
        String contentType = dbFile.getContentType();
        String fileName = dbFile.getFilename();
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        dbFile.writeTo(response.getOutputStream());

    }

    /**
     *
     * Searches for files
     *
     * @param param
     * @return
     * @throws ParseException
     * @throws IOException
     * @throws JsonProcessingException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, value = "/findFiles")
    public List<RequestResponseDocument> findFiles(@RequestParam("params") final String param) throws ParseException,
    JsonProcessingException, IOException {
        RequestResponseDocument document = new RequestResponseDocument();
        log.debug("Received parameters: " + param);
        setParams(param, document);
        List<RequestResponseDocument> documents = new ArrayList<RequestResponseDocument>();
        List<GridFSDBFile> files = service.findDocuments(document);
        for (GridFSDBFile gridFSDBFile : files) {
            setDocumentFields(documents, gridFSDBFile);

        }
        return documents;
    }

    /**
     * uploads file
     *
     *
     * @param file
     * @param tags
     * @throws IOException
     */
    @RequestMapping(value = "/findFiles", method = RequestMethod.POST)
    public void uploadFile(@RequestParam("file") final MultipartFile file, @RequestParam("tags") final String tags)
        throws IOException {
        UploadDocument uploadDocument = new UploadDocument();
        uploadDocument.setFile(file);
        log.debug("Received parameters: " + tags);
        ObjectMapper mapper = new ObjectMapper();
        List<String> tagsList = mapper.readValue(tags, new TypeReference<ArrayList<String>>() {
        });
        log.debug("Parsed list: " + tagsList);
        uploadDocument.setTags(tagsList);
        service.uploadFile(uploadDocument);
    }

    /**
     *
     * deletes file
     *
     * @param id
     * @throws IOException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/findFiles/{id}")
    public void deleteDocument(@PathVariable("id") final String id) throws IOException {
        service.deleteDocument(id);

    }

    /**
     *
     * updates document metadata
     *
     * @param id
     * @param param
     * @throws ParseException
     */
    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, value = "/findFiles/{id}")
    public void updateDocument(@PathVariable("id") final String id,
        @RequestBody final RequestResponseDocument document) throws ParseException {
        log.debug("Received document" + document);
        log.debug("Updating document with _id:  " + id);
        service.updateDocument(id, document);
    }

    /**
     * Stores file metadata in RequestRequestDocument fields
     *
     *
     * @param documents
     * @param gridFSDBFile
     */
    private void setDocumentFields(final List<RequestResponseDocument> documents, final GridFSDBFile gridFSDBFile) {
        RequestResponseDocument document1 = new RequestResponseDocument();
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

    /**
     *
     * Stores request parameters in RequestRequestDocument fields
     *
     * @param param
     * @param document
     * @throws ParseException
     * @throws IOException
     * @throws JsonProcessingException
     */
    private void setParams(final String jsonParams, final RequestResponseDocument document) throws ParseException,
    JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode params = mapper.readTree(jsonParams);
        if (params.get("fileName") != null&& !params.get("fileName").asText().equals("null")) {
            document.setFileName(params.get("fileName").asText());
        }
        if (params.get("startDate") != null) {
            String startDate = params.get("startDate").asText();
            document.setStartDate(parseDate(startDate));
        }
        if (params.get("endDate") != null) {
            String endDate = params.get("endDate").asText();
            document.setEndDate(parseDate(endDate));
        }
        List<String> receivedTags = new ArrayList<String>();
        Iterator<JsonNode> iter = params.get("tags").elements();
        if (iter != null) {

            while (iter.hasNext()) {
                String tag = iter.next().asText();
                if (StringUtils.isNotBlank(tag)) {
                    receivedTags.add(tag);
                }
            }
        }
        document.setTags(receivedTags);
    }

    /**
     * Parses string representatation of the date TODO
     *
     * @param date
     * @return
     * @throws ParseException
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
