package be.ordina.sest.homearchive.service;

import java.io.IOException;

import be.ordina.sest.homearchive.model.UploadDocument;

public interface UploadService {

	void uploadFile(UploadDocument document) throws IOException;
}