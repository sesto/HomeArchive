package be.ordina.sest.homearchive;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by sest on 13/11/14.
 */
public class Constants {
    public static final String ID_1 = "12345";
    public static final String FILE_NAME_1 = "someFile1";
    public static final String CONTENT_TYPE_1 = "application/pdf";
    public static final String DESCRIPTION_1 = "description1";
    public static final String ID_2 = "67889";
    public static final String FILE_NAME_2 = "someFile2";
    public static final String CONTENT_TYPE_2 = "text/xml";
    public static final String DESCRIPTION_2 = "description2";

    public static final Date START_DATE = new GregorianCalendar(2014, 11, 5, 17, 30).getTime();
    public static final Date END_DATE = new GregorianCalendar(2014, 11, 6, 19, 45).getTime();

    public static final Date UPLOAD_DATE_1 = new GregorianCalendar(2014, 11, 7, 0, 0).getTime();
    public static final Date UPLOAD_DATE_2 = new GregorianCalendar(2014, 11, 8, 0, 0).getTime();

}
