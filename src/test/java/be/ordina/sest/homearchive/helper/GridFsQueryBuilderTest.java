package be.ordina.sest.homearchive.helper;

import be.ordina.sest.homearchive.TestHelper;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GridFsQueryBuilderTest {

    private GridFsQueryBuilder builder;


    @Before
    public void init() {
        builder = new GridFsQueryBuilder();

    }

    @Test
    public void testBuilderNoDates() throws Exception {

        builder.addFileName(TestHelper.FILE_NAME_1);
        builder.addContentType(TestHelper.CONTENT_TYPE_1);
        builder.addDescription(TestHelper.DESCRIPTION_1);
        Query query = builder.getQuery();
        DBObject queryObject = query.getQueryObject();
        assertEquals(TestHelper.FILE_NAME_1, queryObject.get("filename").toString());
        assertEquals(TestHelper.CONTENT_TYPE_1, queryObject.get("contentType"));
        assertEquals(TestHelper.DESCRIPTION_1, queryObject.get("metadata.description"));
    }

    @Test
    public void testBuilderOnlyStartDate() {
        builder.addDateRange(TestHelper.START_DATE, null);
        DBObject dateRangeObject = getDateRangeObject();
        assertEquals(TestHelper.BEGINNING_OF_START_DATE, dateRangeObject.get("$gte"));
        assertEquals(TestHelper.END_OF_START_DATE, dateRangeObject.get("$lte"));
        System.out.println(builder.getQuery());
    }

    private DBObject getDateRangeObject() {
        Query query = builder.getQuery();
        DBObject queryObject = query.getQueryObject();
        return (DBObject) queryObject.get("uploadDate");
    }

    @Test
    public void testBuilderStartAndEndDate() {
        builder.addDateRange(TestHelper.START_DATE, TestHelper.END_DATE);
        DBObject dateRangeObject = getDateRangeObject();
        assertEquals(TestHelper.BEGINNING_OF_START_DATE, dateRangeObject.get("$gte"));
        assertEquals(TestHelper.END_END_DATE, dateRangeObject.get("$lte"));
    }

    @Test
    public void testBuilderOnlyEndDate() {
        builder.addDateRange(null, TestHelper.END_DATE);
        DBObject dateRangeObject = getDateRangeObject();
        assertNull(dateRangeObject.get("$gte"));
        assertEquals(TestHelper.END_END_DATE, dateRangeObject.get("$lte"));
    }
}

