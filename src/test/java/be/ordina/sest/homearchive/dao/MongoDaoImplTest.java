package be.ordina.sest.homearchive.dao;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@RunWith(MockitoJUnitRunner.class)
public class MongoDaoImplTest {

	@Mock
	private GridFsTemplate template;

	@InjectMocks
	private MongoDao dao = new MongoDaoImpl();

	@Test
	public void testSaveDocument() throws Exception {
		File file = new File("src/test/resources/testfile.txt");
		String fileName = file.getName();
		String contentType = "txt/xml";
		InputStream is = new FileInputStream(file);

		dao.saveDocument(is, fileName, contentType, new BasicDBObject());
		verify(template, times(1)).store(any(InputStream.class),
				any(String.class), any(String.class), any(DBObject.class));
	}
}
