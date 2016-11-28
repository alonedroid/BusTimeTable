package alonedroid.com.bustimetable.feature;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import alonedroid.com.bustimetable.R;
import alonedroid.com.bustimetable.dao.TimeTableDao;
import alonedroid.com.bustimetable.dao.TimeTableEntity;
import alonedroid.com.bustimetable.databinding.FragmentResultBinding;
import alonedroid.com.bustimetable.databinding.FragmentSearchBinding;
import alonedroid.com.bustimetable.io.BTTAnalyzer;
import alonedroid.com.bustimetable.util.ThreadUtil;

public class BTTResultFragment extends Fragment {

    private static final String ARG_STATION = "argStation";

    private FragmentResultBinding mBinding;
    private ArrayAdapter<String> mAdapter;
    private String key ;

    public static Fragment instantiate(Context context, String station) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_STATION, station);
        return Fragment.instantiate(context, BTTResultFragment.class.getName(), bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false);
        initViews();
        initParameters();
        setTimeTable();
        return mBinding.getRoot();
    }

    private void initViews() {
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
        mBinding.listResultTimeTable.setAdapter(mAdapter);
    }

    private void initParameters(){
        key = getArguments().getString(ARG_STATION);
        Log.d("itinoue", "key load "+key);
    }

    private void setTimeTable(){
        TimeTableEntity entity = TimeTableDao.create(key).getEntity();
        for(TimeTableEntity.TimeTableHour hourTable :entity.bttTimeTableTime){
            for(TimeTableEntity.TimeTableMinute minuteTable:hourTable.ttTimeTableMinute){
                mAdapter.add(entity.bttStation+entity.bttDw+entity.bttDestination+"\n"
                +hourTable.ttHour+":"+minuteTable.ttMinute+"\n"
                +minuteTable.ttSection+minuteTable.ttTo);
            }
        }
    }
}
