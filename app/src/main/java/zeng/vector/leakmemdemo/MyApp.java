package zeng.vector.leakmemdemo;

import android.app.Application;


/**
 * Created by vectorzeng on 2017/11/20.
 */

public class MyApp extends Application {
    public static MyApp sApp= null;
    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }
}
