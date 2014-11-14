package be.ordina.sest.homearchive.rs;

import be.ordina.sest.homearchive.TestHelper;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.service.FileService;
import be.ordina.sest.homearchive.service.SearchService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration(locations = "classpath:application-config.xml")
//@WebAppConfiguration
public class FileRsControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Mock
    private FileService fileService;

    @Mock
    private SearchService searchService;

    @InjectMocks
    private FileRsController fileRsController = new FileRsController();

    private TestHelper testHelper = new TestHelper();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileRsController).build();
    }

    @Test
    public void testDownloadFile() throws Exception {

    }

    @Test
    public void testFindFiles() throws Exception {
        List<RequestResponseDocument> documentList = testHelper.getListOfDocuments();
        when(searchService.findDocuments(any(RequestResponseDocument.class))).thenReturn(documentList);
        mockMvc.perform(get("/findFiles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
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
        verify(searchService,times(1)).findDocuments(any(RequestResponseDocument.class));
    }

    @Test
    public void testUploadFile() throws Exception {

    }

    @Test
    public void testDeleteDocument() throws Exception {

    }

    @Test
    public void testUpdateDocument() throws Exception {

    }
}