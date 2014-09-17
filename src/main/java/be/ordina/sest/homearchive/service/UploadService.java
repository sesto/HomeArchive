package be.ordina.sest.homearchive.service;

import java.io.IOException;

import be.ordina.sest.homearchive.model.DbDocument;

public interface UploadService {

	void uploadFile(DbDocument document) throws IOException;
}