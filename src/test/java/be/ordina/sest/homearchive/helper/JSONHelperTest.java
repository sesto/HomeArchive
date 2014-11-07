package be.ordina.sest.homearchive.helper;

import org.junit.Assert;
import org.junit.Test;

public class JSONHelperTest {

    @Test
    public void testConvertString() throws Exception {
        String expected = "\\\"foo\\\"";
        System.out.println(JSONHelper.convertString("\"foo\""));
        Assert.assertEquals(expected, JSONHelper.convertString("\"foo\""));

    }

}
