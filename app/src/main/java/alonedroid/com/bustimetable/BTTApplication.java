package alonedroid.com.bustimetable;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class BTTApplication  extends Application {

    private static final String SP_KEY = "BusTimeTable";

    public static Context context;
    public static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sp = getSharedPreferences(SP_KEY, MODE_PRIVATE);
        Log.d("itinoue", "oncreate");
    }
}
