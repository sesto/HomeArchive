package be.ordina.sest.homearchive.dao;

import java.io.File;
import java.io.FileNotFoundException;

import com.mongodb.DBObject;

public interface MongoDao {
	/**
	 * Saves document into MongoDB
	 * 
	 * @param metaData
	 *            file metadata
	 * @param file
	 *            file to save
	 */
	void saveDocument(DBObject metaData, File file)
			throws FileNotFoundException;
}