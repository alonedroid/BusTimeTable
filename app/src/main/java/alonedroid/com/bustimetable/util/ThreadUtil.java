package alonedroid.com.bustimetable.util;

import android.content.Context;
import android.os.Handler;

import rx.functions.Action0;

public class ThreadUtil {

    private Handler mHandler;

    public ThreadUtil(){
        mHandler = new Handler();
    }


    public void async(Action0 action) {
        new Thread(action::call).start();
    }


    public void uiThread(Action0 action){
        mHandler.post(action::call);
    }
}
