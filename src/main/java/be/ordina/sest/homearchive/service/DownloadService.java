package be.ordina.sest.homearchive.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import be.ordina.sest.homearchive.model.RequestDocument;

import com.mongodb.gridfs.GridFSDBFile;

public interface DownloadService {

    GridFSDBFile downloadFileById(final RequestDocument downloadDocument) throws IOException;

    List<GridFSDBFile> findDocuments(final RequestDocument document);

    GridFSDBFile findDocumentById(final RequestDocument documents);

}