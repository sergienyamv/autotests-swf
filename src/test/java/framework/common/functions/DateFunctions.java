package framework.common.functions;

import framework.Logger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateFunctions {
    public static final String MM_DD_YYYY = "MM-dd-yyyy";
    public static final String MMM_YYYY = "MMM-yyyy";
    public static final String MM_YYYY = "MM-yyyy";
    static final Logger logger = Logger.getInstance();

    /**
     * get current date in the "dd.MM.yyyy" pattern
     */
    public static String getCurrentDate() {
        return getCurrentDate("dd.MM.yyyy");
    }

    /**
     * get current date in the custom pattern
     */
    public static String getCurrentDate(String pattern) {
        return formatDate(new Date(), pattern);
    }

    public static String getDayFromNow(String dateFromNow, String pattern) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then;
        if (dateFromNow.contains("-")) {
            then = now.minusDays(Long.parseLong(dateFromNow.replace("-", "")));
        } else {
            then = now.plusDays(Long.parseLong(dateFromNow));
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return then.format(formatter);
    }

    /**
     * @param pattern
     * @return
     */
    public static String getCurrentDateEnLocale(String pattern) {
        return new SimpleDateFormat(pattern, new Locale("en", "EN")).format(new Date());
    }

    /**
     * {@link #getCurrentDateEnLocale(String)} with arg "MM-dd-yyyy"
     *
     * @return
     */
    public static String getCurrentDateEnLocale() {
        return getCurrentDateEnLocale(MM_DD_YYYY);
    }

    /**
     * Get the unic suffix based on the current date
     */
    public static String getTimestamp() {
        return getCurrentDate("yyyyMMddHHmmss");
    }

    /**
     * Parse string to the Calendar entity
     *
     * @param s - String to be converted
     */
    public static Calendar dateString2Calendar(String s) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date d1 = new SimpleDateFormat("dd.mm.yyyy").parse(s);
        cal.setTime(d1);
        return cal;
    }

    /**
     * Format date to string using custom pattern
     *
     * @param date    - date to be formatted
     * @param pattern - custom pattern of the date
     */
    public static String formatDate(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * Format date to string using 'en' locale
     *
     * @param date    - date to be formatted
     * @param pattern - custom pattern of the date
     */
    public static String formatDateEnLocation(Date date, String pattern) {
        return new SimpleDateFormat(pattern, new Locale("en", "EN")).format(date);
    }

    /**
     * Format date in the "dd.MM.yyyy" pattern
     *
     * @param date - date to be formatted
     */
    public static Date parseDate(String date) {
        return parseDate(date, "dd.mm.yyyy");
    }

    /**
     * Parse string to the Date entity
     *
     * @param date    - string to be parsed
     * @param pattern custom pattern of the date, according to which string should be parsed
     */
    public static Date parseDate(String date, String pattern) {
        Date result = null;
        try {
            result = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            logger.debug("DateFunctions.parseDate", e);
        }
        return result;
    }

    /**
     * Increase date to the N days
     *
     * @param date date to be increased
     * @param days casesCount of the days
     * @return date increased by N days
     */
    public static Date increaseDateByXDays(final Date date, final int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }
}