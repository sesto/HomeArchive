package be.ordina.sest.homearchive.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import be.ordina.sest.homearchive.dao.MongoDao;
import be.ordina.sest.homearchive.model.DbDocument;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service
public class UploadServiceImpl implements UploadService {
	
	@Autowired
	private MongoDao dao;

	@Override
	public void uploadFile(DbDocument document) throws IOException {
		MultipartFile file = document.getFile();
		String fileName = file.getOriginalFilename();
		String contentType = file.getContentType();
		InputStream content = file.getInputStream();
		DBObject metaData = new BasicDBObject();
		dao.saveDocument(content, fileName, contentType, metaData);
	}

}
