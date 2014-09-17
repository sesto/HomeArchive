package be.ordina.sest.homearchive.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.DBObject;

import be.ordina.sest.homearchive.dao.MongoDao;
import be.ordina.sest.homearchive.model.DbDocument;

@RunWith(MockitoJUnitRunner.class)
public class UploadServiceImplTest {

    @Mock
    private MongoDao mongoDao;

    @Mock
    private MultipartFile file;

    @Mock
    private InputStream is;

    private DbDocument document;

    @Before
    public void init() throws IOException {
        when(file.getOriginalFilename()).thenReturn("fileName");
        when(file.getContentType()).thenReturn("contentType");
        when(file.getInputStream()).thenReturn(is);
        document = new DbDocument();
        document.setFile(file);

    }

    @InjectMocks
    private UploadService uploadService = new UploadServiceImpl();

    @Test
    public void testUploadFile() throws Exception {
        uploadService.uploadFile(document);
        verify(mongoDao, times(1)).saveDocument(any(InputStream.class), any(String.class), any(String.class),
            any(DBObject.class));
    }

}
