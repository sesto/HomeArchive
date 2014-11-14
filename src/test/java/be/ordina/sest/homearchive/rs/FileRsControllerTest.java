package be.ordina.sest.homearchive.rs;

import be.ordina.sest.homearchive.TestHelper;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import be.ordina.sest.homearchive.service.FileService;
import be.ordina.sest.homearchive.service.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
//@ContextConfiguration(locations = "classpath:application-config.xml")
//@WebAppConfiguration
public class FileRsControllerTest {

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
        RequestResponseDocument requestResponseDocument = new RequestResponseDocument();
        when(fileService.findDocuments(requestResponseDocument)).thenReturn(documentList);
        mockMvc.perform(get("/findFiles")).andExpect(status().isOk());
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