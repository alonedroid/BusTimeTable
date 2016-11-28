package alonedroid.com.bustimetable.io;

import alonedroid.com.bustimetable.io.station.StationAnalyzerImpl;
import alonedroid.com.bustimetable.io.station.StationResult;
import alonedroid.com.bustimetable.io.timetable.TimeTableAnalyzerImpl;
import rx.functions.Action0;
import rx.functions.Action1;

public class BTTAnalyzer {

    public static void searchStation(String query, Action1<StationResult> next, Action1<Throwable> error, Action0 complete) {
        StationAnalyzerImpl analyzer = new StationAnalyzerImpl();
        analyzer.report.subscribe(next, error, complete);
        analyzer.execute(query);
    }

    public static void searchTimeTable(String station, String rosen, int dw, Action1<StationResult> next, Action1<Throwable> error, Action0 complete) {
        TimeTableAnalyzerImpl analyzer = new TimeTableAnalyzerImpl();
        analyzer.report.subscribe(next, error, complete);
        analyzer.execute(station, rosen, dw);
    }
}
