package be.ordina.sest.homearchive.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import be.ordina.sest.homearchive.model.DbDocument;
import be.ordina.sest.homearchive.service.UploadService;

@Controller
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@RequestMapping("/uploadForm")
	public ModelAndView getUploadForm(
			@ModelAttribute("dbDocument") DbDocument uploadedFile,
			BindingResult result) {
		return new ModelAndView("uploadForm");
	}

	@RequestMapping("/fileUpload")
	public ModelAndView fileUploaded(
			@ModelAttribute("uploadedFile") DbDocument uploadedFile,
			BindingResult result) throws IOException {
		uploadService.uploadFile(uploadedFile);
		return new ModelAndView("showFile", "message", uploadedFile.getFile().getOriginalFilename());
	}
}
