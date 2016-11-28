package alonedroid.com.bustimetable.io.timetable;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import alonedroid.com.bustimetable.dao.TimeTableDao;
import alonedroid.com.bustimetable.dao.TimeTableEntity;
import alonedroid.com.bustimetable.io.station.StationResult;
import alonedroid.com.bustimetable.util.DateUtil;

public class TimeTableScraper {

    private static final String STATION_TAG = "h2.time";

    private static final String DESTINATION_TAG = ".tab.on";

    private static final String DW_HOLIDAY = ".tt_holiday";

    private static final String DW_SATURDAY = ".tt_saturday";

    private static final String DW_WEEKDAY = ".tt_weekday";

    private static final String TIME_TABLE_TAG = "div.bk_tab_time";

    private static final String TIME_TABLE = "table.timetable2";

    private static final String ROW = "tr";

    private static final String COL_HOUR = "th";

    private static final String COL_INFO = "div.c0";

    private static final String INFO_MINUTE = "b";

    private static final String INFO_ROUTE = "span";

    private static final String INFO_ROUTE_SEPARATOR = "<br>";

    private Element mElement;

    public static boolean isTimeTable(Element root) {
        return !root.select(TIME_TABLE_TAG).isEmpty();
    }

    public TimeTableScraper(Element element) {
        mElement = element;
    }

    public List<StationResult> scraping() {
        ArrayList<StationResult> res = new ArrayList<>();

        String station = scrapeStation();
        Elements tableInfos = scrapeTimeTableInfos();
        Elements timetables = scrapeTimeTable();
        for (int i = 0; i < tableInfos.size(); i++) {
            TimeTableEntity entity = new TimeTableEntity();
            entity.scrapeDate = DateUtil.getYm();
            entity.bttStation = station;
            entity.bttDw = scrapeDw(tableInfos.get(i));
            entity.bttDestination = scrapeDestination(tableInfos.get(i));
            entity.bttTimeTableTime = scrapeTimeTableInfo(timetables.get(i), entity);

            String key = TimeTableDao.createKey(entity);
            TimeTableDao.create(entity).save(key);
            res.add(new StationResult()
                    .setCandidateKey(key)
                    .setCandidateVal(entity.bttDestination)
                    .setArrival(true));
        }

        return res;
    }

    private String scrapeStation() {
        return mElement.select(STATION_TAG).first().text();
    }

    private Elements scrapeTimeTableInfos() {
        return mElement.select(TIME_TABLE_TAG);
    }

    private Elements scrapeTimeTable() {
        return mElement.select(TIME_TABLE);
    }

    private String scrapeDestination(Element element) {
        return element.select(DESTINATION_TAG).first().text();
    }

    private String scrapeDw(Element element) {
        if (!element.select(DW_WEEKDAY).isEmpty()) {
            return TimeTableEntity.DW_WEEKDAY;
        } else if (!element.select(DW_SATURDAY).isEmpty()) {
            return TimeTableEntity.DW_SATURDAY;
        } else if (!element.select(DW_HOLIDAY).isEmpty()) {
            return TimeTableEntity.DW_HOLIDAY;
        }
        return TimeTableEntity.DW_WEEKDAY;
    }

    private ArrayList<TimeTableEntity.TimeTableHour> scrapeTimeTableInfo(Element element, TimeTableEntity entity) {
        ArrayList<TimeTableEntity.TimeTableHour> res = new ArrayList<>();

        for (Element row : scrapeRows(element)) {
            TimeTableEntity.TimeTableHour table = entity.new TimeTableHour();
            table.ttHour = scrapeHour(row);
            if (table.ttHour == null) {
                res.get(res.size() - 1).ttTimeTableMinute.addAll(scrapeTimeTableMinute(row, entity));
            } else {
                table.ttTimeTableMinute = scrapeTimeTableMinute(row, entity);
                res.add(table);
            }
        }

        return res;
    }

    private Elements scrapeRows(Element element) {
        return element.select(ROW);
    }

    private String scrapeHour(Element row) {
        Elements hourElements = row.select(COL_HOUR);
        if (hourElements.isEmpty()) {
            return null;
        }
        return hourElements.first().text();
    }

    private ArrayList<TimeTableEntity.TimeTableMinute> scrapeTimeTableMinute(Element row, TimeTableEntity entity) {
        ArrayList<TimeTableEntity.TimeTableMinute> res = new ArrayList<>();

        for (Element minute : scrapeMinute(row)) {
            TimeTableEntity.TimeTableMinute minTable = entity.new TimeTableMinute();
            minTable.ttMinute = scrapeInfoMinute(minute);
            String[] sectionTo = scrapeSectionTo(minute);
            minTable.ttTo = sectionTo[0];
            minTable.ttSection = sectionTo[1];
            res.add(minTable);
        }

        return res;
    }

    private Elements scrapeMinute(Element row) {
        return row.select(COL_INFO);
    }

    private String scrapeInfoMinute(Element minute) {
        return minute.select(INFO_MINUTE).first().text();
    }

    private String[] scrapeSectionTo(Element minute) {
        String[] res = new String[]{"", ""};

        String[] sectionTo = minute.select(INFO_ROUTE).first().html()
                .replaceAll(" ", "").split(INFO_ROUTE_SEPARATOR);
        if (sectionTo.length >= 2) {
            res = sectionTo;
        } else if (sectionTo.length >= 1) {
            res[0] = sectionTo[0];
        }
        return res;
    }
}
