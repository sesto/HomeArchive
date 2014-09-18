package be.ordina.sest.homearchive.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class GridFsQueryBuilderTest {

    private GridFsQueryBuilder builder;
    private Date startDate;
    private Date endDate;

    @Before
    public void init(){
        builder  = new GridFsQueryBuilder();
        startDate = new DateTime().toDate();
        endDate = new DateTime(startDate).plusDays(1).toDate();
    }

    @Test
    public void testBuilder() throws Exception {
        String fileName = "abc";
        builder.addFileName(fileName);
        System.out.println(builder.getQuery());
        builder.addContentType("txt");
        System.out.println(builder.getQuery());
        List<String> tags = new ArrayList<>();
        tags.add("a tag");
        builder.addTags(tags);
        System.out.println(builder.getQuery());
        builder.addDateRange(startDate, endDate);
        System.out.println(builder.getQuery());
    }
}
