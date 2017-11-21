package zeng.vector.leakmemdemo;

import android.app.Activity;

/**
 * Created by vectorzeng on 2017/11/20.
 */

public class LeakSingleon {
    private static final LeakSingleon ourInstance = new LeakSingleon();

    public static LeakSingleon getInstance() {
        return ourInstance;
    }

    private LeakSingleon() {
    }

    private Activity activity;
    public void init(Activity act){
        this.activity = act;
    }
}
