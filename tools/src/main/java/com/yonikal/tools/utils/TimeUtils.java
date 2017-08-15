package com.yonikal.tools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by yoni on 09/08/2017.
 */
public class TimeUtils {
    private final static String TAG = "TimeUtils";

    public final static String SERVER_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String DESIGN_FORMAT = "dd.MM.yy  HH:mm";
    public final static String DESIGN_FORMAT2 = "HH:mm  dd.MM.yyyy";
    public final static String DESIGN_FORMAT_HH_SS_DD_MM_YY = "HH:mm dd/MM/yy";
    public final static String DESIGN_FORMAT_HH_SS_DD_MM_YYYY = "HH:mm ,dd/MM/yyyy";
    public final static String DESIGN_FORMAT_HH_SS_DD_MM_YYYY2 = "HH:mm ,dd.MM.yyyy";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DATE_FORMAT = "dd.MM.yy";

    // Pre-Defined night time and morning time
    public final static String NIGHT_START = "23:00:00";
    public final static String NIGHT_END = "06:00:00";

    /**
     * A static method to check wether a given time frame has passed from a given time to now
     *
     * @param date       The date
     * @param dateFormat The date format (i.e. dd/MM/yyyy)
     * @param timeFrame  The timeframe to check against
     * @param timeUnit   The time frame unit
     * @return True if the given time frame has passed already false otherwise
     */
    public static boolean isTimeFramePass(String date, String dateFormat, float timeFrame, TimeUnit timeUnit) {
        boolean timePass;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date day = sdf.parse(date);
            timePass = isTimeFramePass(day.getTime(), timeFrame, timeUnit);
        } catch (Exception e) {
            timePass = true;
        }
        return timePass;
    }

    /**
     * A static method to check wether a given time frame has passed from a given time
     *
     * @param timeInMillis The time we want to check
     * @param timeFrame    The time frame to check against (i.e. 24 hours/30 minutes/1 Day etc)
     * @param timeUnit     The time unit for comparison (Seconds/Minutes/Hours/Days)
     * @return True if the given time frame has passed already false otherwise
     * @throws IllegalArgumentException If the the timeUnit parameter is not stated above
     */
    public static boolean isTimeFramePass(long timeInMillis, float timeFrame, TimeUnit timeUnit)
            throws IllegalArgumentException {
        float timeDiff;
//        Date start = new Date(timeInMillis);
        Date currentTime = new Date(System.currentTimeMillis());

        long delta = currentTime.getTime() - timeInMillis;

        switch (timeUnit) {
            case SECONDS:
                timeDiff = TimeUnit.MILLISECONDS.toSeconds(delta);
                break;
            case MINUTES:
                timeDiff = TimeUnit.MILLISECONDS.toMinutes(delta);
                break;
            case HOURS:
                timeDiff = TimeUnit.MILLISECONDS.toHours(delta);
                break;
            case DAYS:
                timeDiff = TimeUnit.MILLISECONDS.toDays(delta);
                break;
            default:
                throw new IllegalArgumentException("The provided time unit is not supported!!");
        }

        boolean timePass = (timeDiff >= timeFrame);
        return timePass;
    }

    /**
     * This method will first check against server parameters
     * in case on of them is missing we will take the defaut parameters
     *
     * @return True in case the current time is night false otherwise
     */
    public static boolean isNight() {
        return isTimeBetweenTwoTime(NIGHT_START, NIGHT_END);
    }

    public static String getFormattedTime(long time) {
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String formattedTime = output.format(time);
        return formattedTime;
    }

    public static String getFormattedTime(long time, String format) {
        SimpleDateFormat output = new SimpleDateFormat(format);
        String formattedTime = output.format(time);
        return formattedTime;
    }


    public static String getFormattedTime() {
        return getFormattedTime(System.currentTimeMillis());
    }

    public static long getMorningWakeupTime(Context context) {
        int hours = 6, minutes = 0, seconds = 0;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String startOfDay = NIGHT_END;
        try {
            String[] values = startOfDay.split(":");
            hours = Integer.parseInt(values[0]);
            minutes = Integer.parseInt(values[1]);
            seconds = Integer.parseInt(values[2]);
        } catch (Exception e) {
            // Do nothing since in case we have a parsing
            // error the variables has already have been assigned
            // this we are using the default
        }

        Calendar date = new GregorianCalendar();
        // Set the time for the requested hour
        date.set(Calendar.HOUR_OF_DAY, hours);
        date.set(Calendar.MINUTE, minutes);
        date.set(Calendar.SECOND, seconds);
        date.set(Calendar.MILLISECOND, 0);

        long resultTime = date.getTimeInMillis();

        // Check if the result is smaller then the current system
        // time if so this time has passed and we need to increment the day value
        // by one
        if (resultTime < System.currentTimeMillis()) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            resultTime = date.getTimeInMillis();
        }


        return resultTime;
    }

    public static int getCurrentDayNumberInMonth() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayInMonth = cal.get(Calendar.MONTH);

        return dayInMonth;
    }

    // Taken From -
    // http://stackoverflow.com/a/28208889
    private static boolean isTimeBetweenTwoTime(String argStartTime, String argEndTime) {
        boolean valid = false;

        String reg = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";

        try {
            if (argStartTime.matches(reg) && argEndTime.matches(reg)) {
                // Start Time
                Date startTime = new SimpleDateFormat(TIME_FORMAT).parse(argStartTime);
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.setTime(startTime);

                // Current Time
                Date currentTime = new Date(System.currentTimeMillis());
                Calendar currentCalendar = Calendar.getInstance();
                currentCalendar.setTime(currentTime);

                // End Time
                Date endTime = new SimpleDateFormat(TIME_FORMAT).parse(argEndTime);
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.setTime(endTime);

                if (currentTime.compareTo(endTime) < 0) {
                    currentCalendar.add(Calendar.DATE, 1);
                    currentTime = currentCalendar.getTime();
                }
                if (startTime.compareTo(endTime) < 0) {
                    startCalendar.add(Calendar.DATE, 1);
                    startTime = startCalendar.getTime();
                }
                if (currentTime.before(startTime)) {
                    valid = false;
                } else {
                    if (currentTime.after(endTime)) {
                        endCalendar.add(Calendar.DATE, 1);
                        endTime = endCalendar.getTime();
                    }
                    valid = currentTime.before(endTime);
                }
                return valid;
            } else {
                throw new IllegalArgumentException("Not a valid time, expecting HH:MM:SS format");
            }
        } catch (ParseException e) {
            Log.e(TAG, "Parse failed on TimeUtils class");
        } finally {
            return valid;
        }
    }

    public static String getTimeDiffString(long timeInMillis) {

        String timeDiffStr;
        long timeDiff = TimeUnit.MILLISECONDS.toMillis(timeInMillis);
        if (timeDiff < 1000) {
            timeDiffStr = timeDiff + " Millis";
        } else if (timeDiff < 60000) {
            timeDiff = TimeUnit.MILLISECONDS.toSeconds(timeInMillis);
            timeDiffStr = timeDiff + " Second/s";
        } else if (timeDiff < 3600000) {
            timeDiff = TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
            timeDiffStr = timeDiff + " Minute/s";
        } else {
            timeDiff = TimeUnit.MILLISECONDS.toHours(timeInMillis);
            timeDiffStr = timeDiff + " Hour/s";
        }

        return timeDiffStr;
    }

    public static boolean isTimePass(String dateToCheck, String fixedDate, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date dayToCheck = sdf.parse(dateToCheck);
            Date dayFixed = sdf.parse(fixedDate);
            if (dayToCheck.getTime() > dayFixed.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String changeDateFormat(String fromDate, String fromTemplate, String toDateTemplate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromTemplate);
        try {
            Date date = dateFormat.parse(fromDate);
            SimpleDateFormat toDateFormat = new SimpleDateFormat(toDateTemplate);
            return toDateFormat.format(date);
        } catch (Exception ex) {
            return null;
        }
    }
}
