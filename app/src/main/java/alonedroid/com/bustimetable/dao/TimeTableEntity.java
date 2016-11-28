package alonedroid.com.bustimetable.dao;

import java.util.ArrayList;

public class TimeTableEntity {

    public static final String DW_HOLIDAY = "日祝";

    public static final String DW_SATURDAY = "土曜";

    public static final String DW_WEEKDAY = "平日";

    public String scrapeDate;

    public String bttStation;

    public String bttDw;

    public String bttDestination;

    public ArrayList<TimeTableHour> bttTimeTableTime;

    public class TimeTableHour {

        public String ttHour;

        public ArrayList<TimeTableMinute> ttTimeTableMinute;
    }

    public class TimeTableMinute {

        public String ttMinute;

        public String ttTo;

        public String ttSection;
    }
}
