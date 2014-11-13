package be.ordina.sest.homearchive.helper;

import java.util.Date;

import org.joda.time.DateTime;

/**
 *
 * Some useful methods for date
 *
 */
public class DateUtils {

    private DateUtils() {

    }

    /**
     *
     * returns beginning of a day
     *
     * @param date
     * @return beginning of a day
     */
    public static Date getBeginningOfDay(final Date date) {
        return new DateTime(date).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
            .toDate();
    }

    /**
     *
     * returns end of a day
     *
     * @param date
     * @return end of a day
     */

    public static Date getEndOfDay(final Date date) {
        DateTime res =
            new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
        return res.toDate();
    }
}
