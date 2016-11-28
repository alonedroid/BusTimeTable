package alonedroid.com.bustimetable.feature;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.LinkedList;

import alonedroid.com.bustimetable.BTTBackableFragment;
import alonedroid.com.bustimetable.MainActivity;
import alonedroid.com.bustimetable.R;
import alonedroid.com.bustimetable.dao.TimeTableDao;
import alonedroid.com.bustimetable.databinding.FragmentSearchBinding;
import alonedroid.com.bustimetable.feature.dialog.BTTWaitDialog;
import alonedroid.com.bustimetable.io.BTTAnalyzer;
import alonedroid.com.bustimetable.io.IBTTApi;
import alonedroid.com.bustimetable.io.station.StationResult;
import alonedroid.com.bustimetable.util.ThreadUtil;

public class BTTSearchFragment extends Fragment implements BTTBackableFragment{

    private BTTWaitDialog mDialog;
    private FragmentSearchBinding mBinding;
    private BTTSearchAdapter mAdapter;
    private ThreadUtil mThreadUtil;
    private LinkedList<String> mSearchedHistory;

    public static Fragment instantiate(Context context) {
        return Fragment.instantiate(context, BTTSearchFragment.class.getName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        init();
        return mBinding.getRoot();
    }

    private void init() {
        mBinding.btnSearch.setOnClickListener(v -> searchBusStop());
        mAdapter = new BTTSearchAdapter(getContext(), android.R.layout.simple_list_item_1);
        mBinding.listSearchStationCandidate.setAdapter(mAdapter);
        mBinding.listSearchStationCandidate.setOnItemClickListener((adapterView, view, pos, id) ->
                clickCandidate(pos)
        );
        mThreadUtil = new ThreadUtil();
        mDialog = new BTTWaitDialog(getContext());
    }

    private void searchBusStop(String busStop){
        mBinding.inputSearchBusStop.setText(busStop);
        mAdapter.clear();
        searchBusStop();
    }

    private void searchBusStop() {
        String busStopName = mBinding.inputSearchBusStop.getText().toString();
        mSearchedHistory.add(busStopName);
        showLoadingDialog();
        mThreadUtil.async(() -> BTTAnalyzer.searchStation(busStopName, this::onNext, this::onError, this::onCompleted));
    }

    private void onNext(StationResult result) {
        mThreadUtil.uiThread(() -> mAdapter.add(result));
    }

    private void onError(Throwable throwable) {
        mThreadUtil.uiThread(() ->
                Toast.makeText(getContext(), "バス停が見つかりませんでした", Toast.LENGTH_SHORT).show());
        dismissLoadingDialog();
        mSearchedHistory.remove();
    }

    private void onCompleted() {
        mThreadUtil.uiThread(() ->
                Toast.makeText(getContext(), "完了", Toast.LENGTH_SHORT).show());
        dismissLoadingDialog();
    }

    private void clickCandidate(int pos) {
        if (mAdapter.isArrival(pos)) {
            showResult(pos);
        } else {
            searchBusStop(mAdapter.getValue(pos));
        }
    }

    private void showResult(int pos) {
        String key = mAdapter.getKey(pos);

        if (TimeTableDao.hasCache(key)) {
            MainActivity.revelation.onNext(BTTResultFragment.instantiate(getActivity(), key));
        } else {
            showLoadingDialog();
            mThreadUtil.async(() -> {
                BTTAnalyzer.searchTimeTable(
                        mAdapter.getStation(pos),
                        mAdapter.getKey(pos),
                        IBTTApi.DW_WEEKDAY,
                        this::onTimeTableNext,
                        this::onTimeTableError,
                        this::onTimeTableCompleted);
            });
        }
    }


    private void onTimeTableNext(StationResult stationResult) {
        MainActivity.revelation.onNext(BTTResultFragment.instantiate(getActivity(), stationResult.getCandidateKey()));
    }

    private void onTimeTableError(Throwable throwable) {
        mThreadUtil.uiThread(() ->
                Toast.makeText(getContext(), "バス停が見つかりませんでした", Toast.LENGTH_SHORT).show());
        dismissLoadingDialog();
    }

    private void onTimeTableCompleted() {
        mThreadUtil.uiThread(() ->
                Toast.makeText(getContext(), "完了", Toast.LENGTH_SHORT).show());
        dismissLoadingDialog();
    }

    private void showLoadingDialog() {
        mDialog.getLoading().onNext(true);
    }

    private void dismissLoadingDialog() {
        mDialog.getLoading().onNext(false);
    }


    @Override
    public boolean onBackPress() {
        if(mSearchedHistory.size() == 0){
            return false;
        }

        searchBusStop(mSearchedHistory.remove());
        return true;
    }
}
