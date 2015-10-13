package it.feio.android.omninotes.utils.date;

import android.content.Context;
import android.text.format.DateUtils;
import it.feio.android.omninotes.utils.Constants;
import roboguice.util.Ln;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Helper per la generazione di date nel formato specificato nelle costanti
 *
 * @author 17000026
 */
public class DateHelper {

    public static String getSortableDate() {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_SORTABLE);
        Date now = Calendar.getInstance().getTime();
        result = sdf.format(now);
        return result;
    }


    public static String getString(long date, String format) {
        Date d = new Date(date);
        return getString(d, format);
    }


    public static String getString(Date d, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(d);
    }


    public static Calendar getDateFromString(String str, String format) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            cal.setTime(sdf.parse(str));
        } catch (ParseException e) {
            Ln.e(e, "Malformed datetime string" + e.getMessage());

        } catch (NullPointerException e) {
            Ln.e(e, "Date or time not set");
        }
        return cal;
    }


    /**
     * Build a formatted date string starting from values obtained by a DatePicker
     *
     * @param year
     * @param month
     * @param day
     * @param format
     * @return
     */
    public static String onDateSet(int year, int month, int day, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        String dateString = sdf.format(cal.getTime());
        return dateString;
    }


    /**
     * Build a formatted time string starting from values obtained by a TimePicker
     *
     * @param hour
     * @param minute
     * @param format
     * @return
     */
    public static String onTimeSet(int hour, int minute, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        String dateString = sdf.format(cal.getTime());
        return dateString;
    }


    public static Calendar getLongFromDateTime(String date, String dateFormat, String time, String timeFormat) {
        Calendar cal = Calendar.getInstance();
        Calendar cDate = Calendar.getInstance();
        Calendar cTime = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat);
        SimpleDateFormat sdfTime = new SimpleDateFormat(timeFormat);
        try {
            cDate.setTime(sdfDate.parse(date));
            cTime.setTime(sdfTime.parse(time));
        } catch (ParseException e) {
            Ln.e(e, "Date or time parsing error: " + e.getMessage());
        }
        cal.set(Calendar.YEAR, cDate.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, cDate.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cDate.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, cTime.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cTime.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, 0);
        return cal;
    }


    public static Calendar getCalendar(Long dateTime) {
        Calendar cal = Calendar.getInstance();
        if (dateTime != null && dateTime != 0) {
            cal.setTimeInMillis(dateTime);
        }
        return cal;
    }


    public static String getLocalizedDateTime(Context mContext,
                                              String dateString, String format) {
        String res = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            sdf = new SimpleDateFormat(Constants.DATE_FORMAT_SORTABLE_OLD);
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e1) {
                Ln.e(e, "String is not formattable into date");
            }
        }

        if (date != null) {
            String dateFormatted = DateUtils.formatDateTime(mContext, date.getTime(), DateUtils.FORMAT_ABBREV_MONTH);
            String timeFormatted = DateUtils.formatDateTime(mContext, date.getTime(), DateUtils.FORMAT_SHOW_TIME);
            res = dateFormatted + " " + timeFormatted;
        }

        return res;
    }


    /**
     * @param mContext
     * @param date
     * @return
     */
    public static String getDateTimeShort(Context mContext, Long date) {
        if (date == null)
            return "";

        Calendar now = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);

        int flags = DateUtils.FORMAT_ABBREV_MONTH;
        if (c.get(Calendar.YEAR) != now.get(Calendar.YEAR))
            flags = flags | DateUtils.FORMAT_SHOW_YEAR;
        return DateUtils.formatDateTime(mContext, date, flags)
                + " " + DateUtils.formatDateTime(mContext, date, DateUtils.FORMAT_SHOW_TIME);
    }


    /**
     * @param mContext
     * @param date
     * @return
     */
    public static String getTimeShort(Context mContext, Long time) {
        if (time == null)
            return "";
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return DateUtils.formatDateTime(mContext, time, DateUtils.FORMAT_SHOW_TIME);
    }


    /**
     * @param mContext
     * @param hourOfDay
     * @param minute
     * @return
     */
    public static String getTimeShort(Context mContext, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        return DateUtils.formatDateTime(mContext, c.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
    }


    public static boolean is24HourMode(Context mContext) {
        boolean res = true;
        Calendar c = Calendar.getInstance();
        String timeFormatted = DateUtils.formatDateTime(mContext, c.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME);
        res = !timeFormatted.toLowerCase().contains("am") && !timeFormatted.toLowerCase().contains("pm");
        return res;
    }


    /**
     * Formats a short time period (minutes)
     *
     * @param time
     * @return
     */
    public static String formatShortTime(Context mContext, long time) {
//		return DateUtils.formatDateTime(mContext, time, DateUtils.FORMAT_SHOW_TIME);
        String m = String.valueOf(time / 1000 / 60);
        String s = String.format("%02d", (time / 1000) % 60);
        return m + ":" + s;
    }

}
