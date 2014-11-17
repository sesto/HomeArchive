package be.ordina.sest.homearchive.rs;

import be.ordina.sest.homearchive.TestHelper;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.model.UploadDocument;
import be.ordina.sest.homearchive.service.FileService;
import be.ordina.sest.homearchive.service.SearchService;
import com.mongodb.gridfs.GridFSDBFile;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class FileRsControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Autowired
    private FileService fileService;

    @Autowired
    private SearchService searchService;


    private GridFSDBFile gridFSDBFile;

    private TestHelper testHelper = new TestHelper();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
    }

    @Test
    public void testDownloadFile() throws Exception {
        RequestResponseDocument doc = new RequestResponseDocument();
        doc.set_id(TestHelper.ID_1);
        GridFSDBFile gridFSDBFile = testHelper.getFile1();
        when(fileService.downloadFileById(eq(doc))).thenReturn(gridFSDBFile);
        mockMvc.perform(get("/findFiles/{id}", TestHelper.ID_1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(TestHelper.CONTENT_TYPE_1)))
                .andExpect(header().string("Content-Disposition", "attachment;filename=" + TestHelper.FILE_NAME_1))
        ;
    }

    @Test
    public void testFindFiles() throws Exception {
        List<RequestResponseDocument> documentList = testHelper.getListOfDocuments();
        when(searchService.findDocuments(any(RequestResponseDocument.class))).thenReturn(documentList);
        mockMvc.perform(get("/findFiles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestHelper.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]._id", is(TestHelper.ID_1)))
                .andExpect(jsonPath("$[0].filename", is(TestHelper.FILE_NAME_1)))
                .andExpect(jsonPath("$[0].contentType", is(TestHelper.CONTENT_TYPE_1)))
                .andExpect(jsonPath("$[0].uploadDate", is(TestHelper.UPLOAD_DATE_1.getTime())))
                .andExpect(jsonPath("$[0].metadata.description", is(TestHelper.DESCRIPTION_1)))
                .andExpect(jsonPath("$[1]._id", is(TestHelper.ID_2)))
                .andExpect(jsonPath("$[1].filename", is(TestHelper.FILE_NAME_2)))
                .andExpect(jsonPath("$[1].contentType", is(TestHelper.CONTENT_TYPE_2)))
                .andExpect(jsonPath("$[1].uploadDate", is(TestHelper.UPLOAD_DATE_2.getTime())))
                .andExpect(jsonPath("$[1].metadata.description", is(TestHelper.DESCRIPTION_2)))
        ;
        verify(searchService, times(1)).findDocuments(any(RequestResponseDocument.class));
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "someFile", "text/xml", "someXml".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/findFiles").file(file).param("description", "description"))
                .andExpect(status().isOk());
        verify(fileService, times(1)).uploadFile(any(UploadDocument.class));

    }

    @Test
    public void testDeleteDocument() throws Exception {
        mockMvc.perform(delete("/findFiles/" + TestHelper.ID_1))
                .andExpect(status().isOk());
        verify(fileService, times(1)).deleteDocument(TestHelper.ID_1);
    }

    @Test
    public void testUpdateDocument() throws Exception {
        RequestResponseDocument requestResponseDocument = testHelper.getDocument1();

        mockMvc.perform(put("/findFiles/{id}", TestHelper.ID_1)
                .contentType(TestHelper.APPLICATION_JSON_UTF8)
                .content(TestHelper.convertObjectToJsonBytes(testHelper.getDocument1())))
                .andExpect(status().isOk())
        ;

        verify(fileService, times(1)).updateDocument(eq(TestHelper.ID_1), eq(requestResponseDocument));
    }

    @Configuration
    @EnableWebMvc
    public static class TestConfiguration {

        @Bean
        public FileRsController fileRsController() {

            return new FileRsController();
        }

        @Bean
        public FileService fileService() {
            return Mockito.mock(FileService.class);
        }

        @Bean
        public SearchService searchService() {
            return Mockito.mock(SearchService.class);
        }

        @Bean
        public MultipartResolver multipartResolver() {
            CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
            return multipartResolver;
        }

    }
}