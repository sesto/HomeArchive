package be.ordina.sest.homearchive.service;

import be.ordina.sest.homearchive.Constants;
import be.ordina.sest.homearchive.dao.MongoDao;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceImplTest {

    @Mock
    private MongoDao mongoDao;

    @Mock
    private MultipartFile file;

    @Mock
    private InputStream is;

    private UploadDocument uploadDocument;
    private RequestResponseDocument requestResponseDocument;



    private GridFSDBFile gridFSDBFile;

    @Before
    public void init() throws IOException {
        uploadDocument = new UploadDocument();
        requestResponseDocument = new RequestResponseDocument();
        requestResponseDocument.set_id(Constants.ID_1);
        requestResponseDocument.setFilename(Constants.FILE_NAME_1);
        requestResponseDocument.setContentType(Constants.CONTENT_TYPE_1);
        requestResponseDocument.getMetadata().setDescription(Constants.DESCRIPTION_1);
        requestResponseDocument.setStartDate(Constants.START_DATE);
        requestResponseDocument.setEndDate(Constants.END_DATE);
        gridFSDBFile = new GridFSDBFile();

    }

    @InjectMocks
    private FileServiceImpl fileService = new FileServiceImpl();

    @Test
    public void testUploadFile() throws Exception {
        when(file.getOriginalFilename()).thenReturn("fileName");
        when(file.getContentType()).thenReturn("contentType");
        when(file.getInputStream()).thenReturn(is);

        uploadDocument.setFile(file);
        fileService.uploadFile(uploadDocument);
        verify(mongoDao, times(1)).saveDocument(any(InputStream.class), any(String.class), any(String.class),
                any(DBObject.class));
    }

    @Test
    public void testDownloadFileById() throws IOException {
        when(mongoDao.findDocument(any(Query.class))).thenReturn(gridFSDBFile);
        GridFSDBFile returnedFile = fileService.downloadFileById(requestResponseDocument);
        assertEquals(returnedFile, gridFSDBFile);
        verify(mongoDao, times(1)).findDocument(any(Query.class));
    }

    @Test
    public void testFindDocuments() {
        fileService.findDocuments(requestResponseDocument);
        verify(mongoDao, times(1)).findDocuments(any(Query.class));
    }

    @Test
    public void testGetMultipleDocumentsQuery() {

        Query query = fileService.getMultipleDocumentQuery(requestResponseDocument);
        DBObject queryObject = query.getQueryObject();
        assertEquals(Constants.ID_1, queryObject.get("_id"));
        assertEquals(Constants.FILE_NAME_1,queryObject.get("filename").toString() );
        assertEquals(Constants.CONTENT_TYPE_1, queryObject.get("contentType"));
        assertEquals(Constants.DESCRIPTION_1, (queryObject.get("metadata.description")));
        DBObject dateRange = (DBObject) queryObject.get("uploadDate");
        Date greaterThenDate = (Date) dateRange.get("$gte");
        assertEquals(DateUtils.truncate(Constants.START_DATE, Calendar.DAY_OF_MONTH), greaterThenDate);
        Date smallerThenDate = (Date) dateRange.get("$lte");
        Date correctedDate = DateUtils.addHours(DateUtils.truncate(Constants.END_DATE, Calendar.DAY_OF_MONTH), 23);
        Date correctedDateMinutes = DateUtils.addMinutes(correctedDate, 59);
        Date correctedDateMinutesSeconds = DateUtils.addSeconds(correctedDateMinutes, 59);
        assertEquals(correctedDateMinutesSeconds.toString(), smallerThenDate.toString());

    }

    @Test
    public void testGetSingleDocumentQuery() {
        requestResponseDocument.set_id(Constants.ID_1);
        Query query = fileService.getSingleDocumentQuery(requestResponseDocument);
        DBObject queryObject = query.getQueryObject();
        assertEquals(Constants.ID_1, queryObject.get("_id"));
    }

    @Test
    public void testSetDocumentFields() {
        //prepare input list
        List<GridFSDBFile> fileList = new ArrayList<>();
        GridFSDBFile file1 = new GridFSDBFile();
        file1.put("_id", Constants.ID_1);
        file1.put("filename", Constants.FILE_NAME_1);
        file1.put("contentType", Constants.CONTENT_TYPE_1);
        file1.put("uploadDate", Constants.UPLOAD_DATE_1);
        file1.put("metadata", new BasicDBObject("description", Constants.DESCRIPTION_1));
        fileList.add(file1);
        GridFSDBFile file2 = new GridFSDBFile();
        file2.put("_id", Constants.ID_2);
        file2.put("filename", Constants.FILE_NAME_2);
        file2.put("contentType", Constants.CONTENT_TYPE_2);
        file2.put("uploadDate", Constants.UPLOAD_DATE_2);
        file2.put("metadata", new BasicDBObject("description", Constants.DESCRIPTION_2));
        fileList.add(file2);

        //prepare expected list
        List<RequestResponseDocument> expectedList = new ArrayList<>();
        RequestResponseDocument doc1 = new RequestResponseDocument();
        doc1.set_id(Constants.ID_1);
        doc1.setFilename(Constants.FILE_NAME_1);
        doc1.setContentType(Constants.CONTENT_TYPE_1);
        doc1.setUploadDate(Constants.UPLOAD_DATE_1);
        doc1.getMetadata().setDescription(Constants.DESCRIPTION_1);
        expectedList.add(doc1);
        RequestResponseDocument doc2 = new RequestResponseDocument();
        doc2.set_id(Constants.ID_2);
        doc2.setFilename(Constants.FILE_NAME_2);
        doc2.setContentType(Constants.CONTENT_TYPE_2);
        doc2.setUploadDate(Constants.UPLOAD_DATE_2);
        doc2.getMetadata().setDescription(Constants.DESCRIPTION_2);
        expectedList.add(doc2);
        assertEquals(expectedList, fileService.setDocumentFields(fileList));

    }

    @Test
    public void testDeleteDocument() throws Exception {
        fileService.deleteDocument("12345");
        verify(mongoDao, times(1)).deleteDocument(any(Query.class));
    }

    @Test
    public void testUpdateDocument() throws Exception {
        fileService.updateDocument(Constants.ID_1, requestResponseDocument);
        verify(mongoDao, times(1)).updateDocument(eq(Constants.ID_1), any(DBObject.class));
    }

    @Test
    public void testGetUpdateQuery() throws Exception {
        DBObject expectedFields = new BasicDBObject();
        expectedFields.put("filename", Constants.FILE_NAME_1);
        expectedFields.put("metadata", new BasicDBObject("description", Constants.DESCRIPTION_1));
        DBObject expectedUpdate = new BasicDBObject("$set", expectedFields);
        assertEquals(expectedUpdate, fileService.getUpdateQuery(requestResponseDocument));
    }
}
