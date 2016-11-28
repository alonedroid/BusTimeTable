package alonedroid.com.bustimetable.io.station;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

class StationRouteScraper {

    private static final String STATION_TAG = "h2.time";

    private static final String LI_TAG = "ul.linename_bus1 li a";

    private static final String ROUTE_TAG = "div.bk_rosen_list";

    private Element mElement;

    static boolean isRoute(Element root) {
        return !root.select(ROUTE_TAG).isEmpty();
    }

    StationRouteScraper(Element element) {
        mElement = element;
    }

    List<StationResult> scraping() {
        return scrapeText(scrapeStation(), scrapeRoutes());
    }

    private String scrapeStation() {
        return mElement.select(STATION_TAG).first().text();
    }

    private Elements scrapeRoutes() {
        return mElement.select(LI_TAG);
    }

    private ArrayList<StationResult> scrapeText(String station, Elements elements) {
        ArrayList<StationResult> res = new ArrayList<>();

        for (Element element : elements) {
            res.add(new StationResult()
                    .setCandidateKey(element.text())
                    .setCandidateVal(element.text())
                    .setArrival(true)
                    .setStation(station));
        }

        return res;
    }
}
