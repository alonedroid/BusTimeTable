package alonedroid.com.bustimetable.io.station;

import java.io.IOException;
import java.util.List;

import alonedroid.com.bustimetable.io.IBTTAnalyzer;
import alonedroid.com.bustimetable.io.IBTTApi;
import alonedroid.com.bustimetable.util.DateUtil;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.subjects.PublishSubject;

public class StationAnalyzerImpl implements IBTTAnalyzer {

    public static final String ARRIVAL = "arrival";

    public PublishSubject<StationResult> report = PublishSubject.create();

    public void execute(String station) {
        Response<List<StationResult>> response = search(station);

        // 失敗
        if (response == null || response.body() == null || !response.isSuccessful()) {
            report.onError(new Throwable());
            return;
        }

        // 成功
        for (StationResult entity : response.body()) {
            report.onNext(entity);
        }
        report.onCompleted();
    }

    private Response<List<StationResult>> search(String station) {
        try {
            return buildRetrofit()
                    .create(IBTTApi.class)
                    .getStationName(station, DateUtil.getDym(), DateUtil.getDdd())
                    .execute();
        } catch (IOException e) {
            return null;
        }
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(StationConverterFactory.create())
                .build();
    }
}
