package be.ordina.sest.homearchive.helper;

import be.ordina.sest.homearchive.TestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateUtilsTest {

    @Test
    public void testGetBeginningOfDay() throws Exception {
        assertEquals(TestHelper.BEGINNING_OF_START_DATE, DateUtils.getBeginningOfDay(TestHelper.START_DATE));


    }

    @Test
    public void testGetEndOfDay() throws Exception {
        assertEquals(TestHelper.END_OF_START_DATE, DateUtils.getEndOfDay(TestHelper.START_DATE));
    }
}