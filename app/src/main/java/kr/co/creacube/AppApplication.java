package kr.co.creacube;

import android.app.Application;
import android.content.Context;

import kr.co.creacube.util.SharedPrefUtil;

public class AppApplication extends Application {
    private static AppApplication instance;
    private static Context context;

    private Boolean isBackground = false;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        context = getApplicationContext();

        SharedPrefUtil.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static AppApplication getInstance() {
        return instance;
    }
    public static Context getContext() {
        return context;
    }
}
