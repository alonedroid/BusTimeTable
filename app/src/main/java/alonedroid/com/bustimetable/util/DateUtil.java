package alonedroid.com.bustimetable.util;

import android.text.format.DateFormat;

import java.util.Calendar;

public class DateUtil {

    public static String getYm() {
        return DateFormat.format("yyyyMM", Calendar.getInstance()).toString();
    }

    public static String getDym() {
        return DateFormat.format("yyyyMM", Calendar.getInstance()).toString();
    }

    public static String getDdd() {
        return DateFormat.format("dd", Calendar.getInstance()).toString();
    }
}
