package be.ordina.sest.homearchive.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import be.ordina.sest.homearchive.Constants;
import com.mongodb.DBObject;
import org.apache.commons.lang.time.*;
import be.ordina.sest.homearchive.helper.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import org.springframework.data.mongodb.core.query.Query;

public class GridFsQueryBuilderTest {

    private GridFsQueryBuilder builder;


    @Before
    public void init() {
        builder = new GridFsQueryBuilder();

    }

    @Test
    public void testBuilderNoDates() throws Exception {

        builder.addFileName(Constants.FILE_NAME_1);
        builder.addContentType(Constants.CONTENT_TYPE_1);
        builder.addDescription(Constants.DESCRIPTION_1);
        Query query = builder.getQuery();
        DBObject queryObject = query.getQueryObject();
        assertEquals(Constants.FILE_NAME_1, queryObject.get("filename").toString());
        assertEquals(Constants.CONTENT_TYPE_1, queryObject.get("contentType"));
        assertEquals(Constants.DESCRIPTION_1, queryObject.get("metadata.description"));

    }

    @Test
    public void testBuilderOnlyStartDate() {
        builder.addDateRange(Constants.START_DATE, null);
        DBObject dateRangeObject = getDateRangeObject();
        assertEquals(DateUtils.getBeginningOfDay(Constants.START_DATE), dateRangeObject.get("$gte"));
        assertEquals(DateUtils.getEndOfDay(Constants.START_DATE), dateRangeObject.get("$lte"));
        System.out.println(builder.getQuery());
    }

    private DBObject getDateRangeObject() {
        Query query = builder.getQuery();
        DBObject queryObject = query.getQueryObject();
        return (DBObject) queryObject.get("uploadDate");
    }

    @Test
    public void testBuilderStartAndEndDate(){
        builder.addDateRange(Constants.START_DATE, Constants.END_DATE);
        DBObject dateRangeObject = getDateRangeObject();
        assertEquals(DateUtils.getBeginningOfDay(Constants.START_DATE), dateRangeObject.get("$gte"));
        assertEquals(DateUtils.getEndOfDay(Constants.END_DATE), dateRangeObject.get("$lte"));
    }
    @Test
    public void testBuilderOnlyEndDate(){
        builder.addDateRange(null, Constants.END_DATE);
        DBObject dateRangeObject = getDateRangeObject();
        assertNull(dateRangeObject.get("$gte"));
        assertEquals(DateUtils.getEndOfDay(Constants.END_DATE), dateRangeObject.get("$lte"));
    }
}

