package be.ordina.sest.homearchive.helper;

import java.util.Date;

import org.joda.time.DateTime;

public class DateUtils {



    private DateUtils() {

    }

    public static Date getBeginningOfDay(final Date beginningDate) {
        return new DateTime(beginningDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
            .withMillisOfSecond(0).toDate();
    }

    public static Date getEndOfDay(final Date date) {
        DateTime res =
            new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
        return res.toDate();
    }
}
