package be.ordina.sest.homearchive.service;

import be.ordina.sest.homearchive.TestHelper;
import be.ordina.sest.homearchive.dao.MongoDao;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
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
    private TestHelper testHelper;


    private GridFSDBFile gridFSDBFile;

    @Before
    public void init() throws IOException {
        testHelper = new TestHelper();
        uploadDocument = new UploadDocument();
        requestResponseDocument = testHelper.getDocument1();
        requestResponseDocument.setStartDate(TestHelper.START_DATE);
        requestResponseDocument.setEndDate(TestHelper.END_DATE);
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
        assertEquals(TestHelper.ID_1, queryObject.get("_id"));
        assertEquals(TestHelper.FILE_NAME_1, queryObject.get("filename").toString());
        assertEquals(TestHelper.CONTENT_TYPE_1, queryObject.get("contentType"));
        assertEquals(TestHelper.DESCRIPTION_1, (queryObject.get("metadata.description")));
        DBObject dateRange = (DBObject) queryObject.get("uploadDate");
        Date greaterThenDate = (Date) dateRange.get("$gte");
        assertEquals(TestHelper.BEGINNING_OF_START_DATE, greaterThenDate);
        Date smallerThenDate = (Date) dateRange.get("$lte");
        assertEquals(TestHelper.END_END_DATE, smallerThenDate);

    }

    @Test
    public void testGetSingleDocumentQuery() {
        requestResponseDocument.set_id(TestHelper.ID_1);
        Query query = fileService.getSingleDocumentQuery(requestResponseDocument);
        DBObject queryObject = query.getQueryObject();
        assertEquals(TestHelper.ID_1, queryObject.get("_id"));
    }

    @Test
    public void testSetDocumentFields() {
        //prepare input list
        List<GridFSDBFile> fileList = testHelper.getFileList();
        List<RequestResponseDocument> expectedList = testHelper.getListOfDocuments();
        assertEquals(expectedList, fileService.setDocumentFields(fileList));

    }

    @Test
    public void testDeleteDocument() throws Exception {
        fileService.deleteDocument("12345");
        verify(mongoDao, times(1)).deleteDocument(any(Query.class));
    }

//    @Test
//    public void testUpdateDocument() throws Exception {
//        fileService.updateDocument(TestHelper.ID_1, requestResponseDocument);
//        verify(mongoDao, times(1)).updateDocument(eq(TestHelper.ID_1), any(DBObject.class));
//    }
//
//    @Test
//    public void testGetUpdateQuery() throws Exception {
//        DBObject expectedFields = new BasicDBObject();
//        expectedFields.put("filename", TestHelper.FILE_NAME_1);
//        expectedFields.put("metadata", new BasicDBObject("description", TestHelper.DESCRIPTION_1));
//        DBObject expectedUpdate = new BasicDBObject("$set", expectedFields);
//        assertEquals(expectedUpdate, fileService.getUpdateQuery(requestResponseDocument));
//    }
}
