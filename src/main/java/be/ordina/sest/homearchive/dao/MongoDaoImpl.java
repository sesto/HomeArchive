package be.ordina.sest.homearchive.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;

/**
 * Dao for CRUD MongoDB operations
 * 
 * @author sest
 *
 */
@Component
public class MongoDaoImpl implements MongoDao {
	@Autowired
	private GridFsTemplate template;

	@Override
	public void saveDocument(DBObject metaData, File file)
			throws FileNotFoundException {
		InputStream io = new FileInputStream(file);
		String fileName = file.getName();
		template.store(io, fileName, metaData);

	}

}
