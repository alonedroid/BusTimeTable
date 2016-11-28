package alonedroid.com.bustimetable.io.timetable;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

import alonedroid.com.bustimetable.io.station.StationResult;
import alonedroid.com.bustimetable.io.timetable.TimeTableScraper;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.http.Headers;

public class TimeTableResponseBodyConverter implements Converter<ResponseBody, List<StationResult>> {

    private static final String CONTENTS = "contents";

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36")
    @Override
    public List<StationResult> convert(ResponseBody value) throws IOException {
        Element root = Jsoup.parse(value.string()).getElementById(CONTENTS);
        return new TimeTableScraper(root).scraping();
    }
}