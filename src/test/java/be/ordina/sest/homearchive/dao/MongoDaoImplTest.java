package be.ordina.sest.homearchive.dao;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
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
		dao.saveDocument(new BasicDBObject(), new File("src/test/resources/testfile.txt"));
		verify(template, times(1)).store(any(InputStream.class), any(String.class), any(DBObject.class));
	}
}
