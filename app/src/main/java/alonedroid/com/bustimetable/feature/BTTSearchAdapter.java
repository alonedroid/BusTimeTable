package alonedroid.com.bustimetable.feature;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import alonedroid.com.bustimetable.io.station.StationResult;

public class BTTSearchAdapter extends ArrayAdapter<String> {

    private ArrayList<StationResult> mData = new ArrayList<>();

    public BTTSearchAdapter(Context context, int resource) {
        super(context, resource);
    }

    public void add(StationResult result) {
        mData.add(result);
        super.add(result.getCandidateVal());
    }

    @Override
    public void clear() {
        mData = new ArrayList<>();
        super.clear();
    }

    public String getKey(int position) {
        String key = mData.get(position).getCandidateKey().replaceAll("（", "(");
        if (!key.contains("(")) {
            return key;
        }

        return key.substring(0, key.indexOf("("));
    }

    public String getValue(int position) {
        return mData.get(position).getCandidateVal();
    }

    public boolean isArrival(int position) {
        return mData.get(position).isArrival();
    }

    public String getStation(int position) {
        return mData.get(position).getStation();
    }
}
