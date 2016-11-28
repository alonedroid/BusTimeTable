package alonedroid.com.bustimetable.io.station;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

import alonedroid.com.bustimetable.io.timetable.TimeTableScraper;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class StationResponseBodyConverter implements Converter<ResponseBody, List<StationResult>> {

    private static final String CONTENTS = "contents";

    @Override
    public List<StationResult> convert(ResponseBody value) throws IOException {
        Element root = Jsoup.parse(value.string()).getElementById(CONTENTS);

        if (StationCandidateScraper.isCandidate(root)) {
            return new StationCandidateScraper(root).scraping();
        } else if (StationRouteScraper.isRoute(root)) {
            return new StationRouteScraper(root).scraping();
        } else if (TimeTableScraper.isTimeTable(root)) {
            return new TimeTableScraper(root).scraping();
        }

        return null;
    }
}