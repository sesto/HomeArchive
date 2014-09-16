package be.ordina.sest.homearchive.dao;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application-config.xml"})
public class MongoDaoImplSpringTest {

	@Autowired
	private MongoDao dao;

	@Test
	public void testSaveDocument() throws Exception {
		DBObject metaData = new BasicDBObject("tag", "abc");
		File testFile = new File("src/test/resources/testfile.txt");
		dao.saveDocument(metaData, testFile);
	}

}
