package be.ordina.sest.homearchive.service;

import java.io.IOException;

import be.ordina.sest.homearchive.model.DownloadDocument;

public interface DownloadService {

    void downloadFileByName(final DownloadDocument downloadDocument) throws IOException;

}