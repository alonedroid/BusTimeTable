package alonedroid.com.bustimetable.io.timetable;

import java.io.IOException;
import java.util.List;

import alonedroid.com.bustimetable.io.IBTTAnalyzer;
import alonedroid.com.bustimetable.io.IBTTApi;
import alonedroid.com.bustimetable.io.station.StationResult;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.subjects.PublishSubject;

public class TimeTableAnalyzerImpl implements IBTTAnalyzer {

    public PublishSubject<StationResult> report = PublishSubject.create();

    public void execute(String station, String rosen, int dw) {
        Response<List<StationResult>> response = search(station, rosen, dw);
        if (response == null || !response.isSuccessful()) {
            report.onError(new Throwable());
            return;
        }
        for (StationResult entity : response.body()) {
            report.onNext(entity);
        }
        report.onCompleted();
    }

    private Response<List<StationResult>> search(String station, String rosen, int dw) {
        try {
            return buildRetrofit()
                    .create(IBTTApi.class)
                    .getTimeTable(station, rosen, dw)
                    .execute();
        } catch (IOException e) {
            return null;
        }
    }

    private Retrofit buildRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TimeTableConverterFactory.create())
                .build();
    }
}
