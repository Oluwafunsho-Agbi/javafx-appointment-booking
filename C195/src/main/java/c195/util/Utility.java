package c195.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is a utility class which contains
 * several methods that are commonly used by
 * other classes. The class contains general methods to be used by other classes
 * The Database related methods are present in the DBUtility class
 * The main intent of this class is to avoid code duplicacy and code reuse
 * easily
 */
public class Utility {
    /**
     *
     * @param number A string possibly representing a number
     * @return true if the string provided is an integer otherwise
     *          returns false
     */
    public static boolean isNumeric(String number) {
        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param localDateTime The localDateTime which needs to be converted to a different timeZone
     * @param fromZoneID The current timezone of the provided localDateTime
     * @param toZoneID The ZoneId to which the given localDateTime is to be converted to
     * @return LocalDateTime converted to toZoneID
     */
    public static LocalDateTime convertZoneLocalDateTime(LocalDateTime localDateTime, ZoneId fromZoneID, ZoneId toZoneID) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(fromZoneID).withZoneSameInstant(toZoneID);
        return zonedDateTime.toLocalDateTime();
    }

    /**
     *
     * @return The current time in yyyy-MM-dd HH:mm:ss String format in UTC
     */
    public static String getCurrentTimeInUTC() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     *
     * @param dateTime A String representing dataTime in the format yyyy-MM-dd HH:mm:ss
     * @return The month represented by this date with 0 being Jan and 11 being Dec
     */
    public static Integer getMonth(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter);
        return localDateTime.getMonth().getValue() - 1;
    }
}
