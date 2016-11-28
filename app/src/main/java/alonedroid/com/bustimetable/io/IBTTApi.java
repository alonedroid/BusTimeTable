package alonedroid.com.bustimetable.io;

import java.util.List;

import alonedroid.com.bustimetable.io.station.StationResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IBTTApi {

    public static final int DW_WEEKDAY = 1;
    public static final int DW_WEEKEND = 2;
    public static final int DW_HOLIDAY = 3;

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36")
    @GET("/bus/rosen/search?eok1=&Cmap1=&rf=tm&pg=5&Dw=0&S.x=0&S.y=0&S=検索")
    Call<List<StationResult>> getStationName(
            @Query("eki1") final String eki1,
            @Query("Dym") final String dym,
            @Query("Ddd") final String ddd
    );

    @Headers("User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36")
    @GET("/bus/rosen/timetable/{departure}/{rosen}/")
    Call<List<StationResult>> getTimeTable(
            @Path("departure") final String departure,
            @Path("rosen") final String rosen,
            @Query("Dw") final int dw
    );
}
