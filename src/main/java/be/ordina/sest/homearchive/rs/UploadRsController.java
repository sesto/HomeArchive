package be.ordina.sest.homearchive.rs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import be.ordina.sest.homearchive.model.UploadDocument;
import be.ordina.sest.homearchive.service.UploadService;

@RestController
public class UploadRsController {
    @Autowired
    private UploadService uploadService;

    @RequestMapping(value = "/findFiles", method = RequestMethod.POST)
    public void uploadFile(@RequestParam("file") final MultipartFile file, @RequestParam("tags") final Object tags)
        throws IOException {
        UploadDocument uploadDocument = new UploadDocument();
        uploadDocument.setFile(file);
        @SuppressWarnings("unchecked")
        HashMap<String, String> tagsMap = new ObjectMapper().readValue(tags.toString(), HashMap.class);
        List<String> tagsList = new ArrayList<>();
        for (String value : tagsMap.values()) {
            tagsList.add(value);
        }
        uploadDocument.setTags(tagsList);
        uploadService.uploadFile(uploadDocument);
    }
}
