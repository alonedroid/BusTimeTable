package alonedroid.com.bustimetable.io.station;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 駅が複数あった場合に、候補を抽出するクラス
 */
class StationCandidateScraper {

    private static final String CANDIDATE_TAG = "div.kouho";

    private static final String OPTION = "option";

    private static final String EKI_IN = "eki1_in";

    private Element mElement;

    static boolean isCandidate(Element root) {
        return !root.select(CANDIDATE_TAG).isEmpty();
    }

    StationCandidateScraper(Element element) {
        mElement = element;
    }

    List<StationResult> scraping() {
        return scrapeOptionValues(scrapeCandidates());
    }

    private Elements scrapeCandidates() {
        return mElement.getElementById(EKI_IN).select(OPTION);
    }

    private List<StationResult> scrapeOptionValues(Elements elements) {
        ArrayList<StationResult> res = new ArrayList<>();
        for (Element element : elements) {
            res.add(new StationResult()
                    .setCandidateKey(element.text())
                    .setCandidateVal(element.text()));
        }
        return res;
    }
}
