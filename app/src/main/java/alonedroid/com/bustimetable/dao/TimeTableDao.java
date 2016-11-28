package alonedroid.com.bustimetable.dao;

import android.text.TextUtils;

import com.google.gson.Gson;

import alonedroid.com.bustimetable.BTTApplication;
import alonedroid.com.bustimetable.util.DateUtil;

public class TimeTableDao {

    private static final String PREFIX_KEY = "bttStation:";

    private TimeTableEntity mEntity;

    public static TimeTableDao create(String key) {
        TimeTableDao dao = new TimeTableDao();
        dao.mEntity = load(key);
        return dao;
    }

    public static TimeTableDao create(TimeTableEntity entity) {
        TimeTableDao dao = new TimeTableDao();
        dao.mEntity = entity;
        return dao;
    }

    private static TimeTableEntity load(String key) {
        String strEntity = BTTApplication.sp.getString(key, "");
        if (TextUtils.isEmpty(strEntity)) return new TimeTableEntity();
        return new Gson().fromJson(strEntity, TimeTableEntity.class);
    }

    public void save(String key) {
        BTTApplication.sp.edit()
                .putString(key, new Gson().toJson(mEntity))
                .apply();
    }

    public TimeTableEntity getEntity() {
        return mEntity;
    }

    public static String createKey(TimeTableEntity entity) {
        return PREFIX_KEY + entity.bttStation + entity.bttDw + entity.bttDestination;
    }

    public static boolean isStationKey(String arg) {
        return arg.startsWith(PREFIX_KEY);
    }

    public static boolean hasCache(String key) {
        TimeTableEntity entity = load(key);
        return !(TextUtils.isEmpty(entity.scrapeDate) || !entity.scrapeDate.equals(DateUtil.getYm()));
    }
}
