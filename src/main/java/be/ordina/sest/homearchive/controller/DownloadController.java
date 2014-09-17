package be.ordina.sest.homearchive.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import be.ordina.sest.homearchive.model.DownloadDocument;
import be.ordina.sest.homearchive.model.UploadDocument;
import be.ordina.sest.homearchive.service.DownloadService;

@Controller
public class DownloadController {

    @Autowired
    private DownloadService service;

    @RequestMapping("/downloadForm")
    public ModelAndView getDownlodForm(@ModelAttribute("downloadDocument") final UploadDocument uploadedFile,
        final BindingResult result) {
        return new ModelAndView("downloadForm");
    }

    @RequestMapping("/fileDownload")
    public ModelAndView fileUploaded(@ModelAttribute("downloadDocument") final DownloadDocument downloadDocument,
        final BindingResult result) throws IOException {
        service.downloadFileByName(downloadDocument);
        return null;
        // new ModelAndView("showFile", "message", uploadedFile.getFile().getOriginalFilename());
    }
}
