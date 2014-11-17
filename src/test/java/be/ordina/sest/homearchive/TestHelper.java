package be.ordina.sest.homearchive;

import be.ordina.sest.homearchive.model.RequestResponseDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFSDBFile;

import org.joda.time.DateTime;


import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by sest on 13/11/14.
 */
public class TestHelper {

    public static final String ID_1 = "12345";
    public static final String FILE_NAME_1 = "someFile1";
    public static final String CONTENT_TYPE_1 = "application/pdf";
    public static final String DESCRIPTION_1 = "description1";
    public static final String ID_2 = "67889";
    public static final String FILE_NAME_2 = "someFile2";
    public static final String CONTENT_TYPE_2 = "text/xml";
    public static final String DESCRIPTION_2 = "description2";

    public static final Date START_DATE = new DateTime(2014, 11, 5, 17, 30).toDate();
    public static final Date BEGINNING_OF_START_DATE = new DateTime(2014, 11, 5,0,0).toDate();
    public static final Date END_OF_START_DATE = new DateTime(2014, 11, 5, 23, 59, 59, 999).toDate();
    public static final Date END_DATE = new DateTime(2014, 11, 6, 19, 45).toDate();
    public static final Date END_END_DATE = new DateTime(2014, 11, 6, 23, 59, 59, 999).toDate();

    public static final Date UPLOAD_DATE_1 = new DateTime(2014, 11, 7, 0, 0).toDate();
    public static final Date UPLOAD_DATE_2 = new DateTime(2014, 11, 8, 0, 0).toDate();

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));



     public RequestResponseDocument getDocument1(){
        RequestResponseDocument requestResponseDocument = new RequestResponseDocument();
        requestResponseDocument.set_id(ID_1);
        requestResponseDocument.setFilename(FILE_NAME_1);
        requestResponseDocument.setContentType(CONTENT_TYPE_1);
        requestResponseDocument.setUploadDate(UPLOAD_DATE_1);
        requestResponseDocument.getMetadata().setDescription(DESCRIPTION_1);
        return requestResponseDocument;
    }

    public RequestResponseDocument getDocument2(){
        RequestResponseDocument requestResponseDocument = new RequestResponseDocument();
        requestResponseDocument.set_id(ID_2);
        requestResponseDocument.setFilename(FILE_NAME_2);
        requestResponseDocument.setContentType(CONTENT_TYPE_2);
        requestResponseDocument.setUploadDate(UPLOAD_DATE_2);
        requestResponseDocument.getMetadata().setDescription(DESCRIPTION_2);
        return requestResponseDocument;
    }

    public List<RequestResponseDocument> getListOfDocuments(){
        List<RequestResponseDocument> requestResponseDocuments = new ArrayList<>();
        requestResponseDocuments.add(getDocument1());
        requestResponseDocuments.add(getDocument2());
        return requestResponseDocuments;
    }

    public GridFSDBFile getFile1(){
        GridFSDBFile file = new GridFSDBFile();
        file.put("_id", ID_1);
        file.put("filename", FILE_NAME_1);
        file.put("contentType", CONTENT_TYPE_1);
        file.put("uploadDate", UPLOAD_DATE_1);
        file.put("metadata", new BasicDBObject("description", DESCRIPTION_1));
        return  file;
    }


    public GridFSDBFile getFile2(){
        GridFSDBFile file = new GridFSDBFile();
        file.put("_id", ID_2);
        file.put("filename", FILE_NAME_2);
        file.put("contentType", CONTENT_TYPE_2);
        file.put("uploadDate", UPLOAD_DATE_2);
        file.put("metadata", new BasicDBObject("description", DESCRIPTION_2));
        return  file;
    }

    public List<GridFSDBFile> getFileList(){
        List<GridFSDBFile> fileList = new ArrayList<>();
        fileList.add(getFile1());
        fileList.add(getFile2());
        return fileList;
    }



    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }
}
