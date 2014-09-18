package be.ordina.sest.homearchive.controller;

import java.io.IOException;
import java.util.List;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.gridfs.GridFSDBFile;

import be.ordina.sest.homearchive.model.RequestDocument;
import be.ordina.sest.homearchive.model.UploadDocument;
import be.ordina.sest.homearchive.service.DownloadService;

@Controller
@Log4j
public class DownloadController {

    @Autowired
    private DownloadService service;

    @RequestMapping("/downloadForm")
    public ModelAndView getDownlodForm(@ModelAttribute("downloadDocument") final UploadDocument uploadedFile,
        final BindingResult result) {
        return new ModelAndView("downloadForm");
    }

    @RequestMapping("/fileDownload")
    public ModelAndView fileUploaded(@ModelAttribute("downloadDocument") final RequestDocument downloadDocument,
        final BindingResult result) throws IOException {
        //        service.downloadFileByName(downloadDocument);
        List<GridFSDBFile> listOfFiles = service.findDocuments(downloadDocument);
        log.info("Found files: " + listOfFiles);
        return null;
        // new ModelAndView("showFile", "message", uploadedFile.getFile().getOriginalFilename());
    }
}
